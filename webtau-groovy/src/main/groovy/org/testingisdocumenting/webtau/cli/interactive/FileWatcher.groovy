/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twosigma.webtau.cli.interactive

import com.sun.nio.file.SensitivityWatchEventModifier
import com.twosigma.webtau.console.ConsoleOutputs
import com.twosigma.webtau.reporter.stacktrace.StackTraceUtils
import groovy.io.FileType

import java.nio.file.*
import java.util.concurrent.atomic.AtomicReference

import static java.nio.file.StandardWatchEventKinds.*

class FileWatcher {
    private final WatchService watchService
    private final Path rootDirToWatch
    private final Map<WatchKey, Path> pathByKey
    private final OnFileChangeHandler onFileChangeHandler

    private final AtomicReference isStopped = new AtomicReference()

    FileWatcher(Path rootDirToWatch, OnFileChangeHandler onFileChangeHandler) {
        this.onFileChangeHandler = onFileChangeHandler

        watchService = createWatchService()
        pathByKey = new HashMap<>()
        this.rootDirToWatch = rootDirToWatch.toAbsolutePath()

        registerDirs(this.rootDirToWatch)
    }

    void start() {
        try {
            startWatchLoop()
        } catch (InterruptedException e) {
            throw new RuntimeException(e)
        }
    }

    void stop() {
        isStopped.set(true)
    }

    private void startWatchLoop() throws InterruptedException {
        while (true) {
            try {
                watchCycle()
                if (isStopped.get()) {
                    return
                }
            } catch (RuntimeException e) {
                ConsoleOutputs.err(StackTraceUtils.renderStackTrace(e))
            }
        }
    }

    private void watchCycle() throws InterruptedException {
        final WatchKey key = watchService.take()
        try {
            final Path path = pathByKey.get(key)
            if (path == null) {
                ConsoleOutputs.err("bad watch key: ", key)
                return
            }

            key.pollEvents().each { e -> handleEvent(path, e) }
        } finally {
            boolean isValid = key.reset()
            if (!isValid) {
                pathByKey.remove(key)
            }
        }
    }

    private void handleEvent(Path parentPath, final WatchEvent<?> watchEvent) {
        final WatchEvent.Kind<?> kind = watchEvent.kind()
        if (kind == OVERFLOW) {
            return
        }

        @SuppressWarnings("unchecked")
        final Path relativePath = ((WatchEvent<Path>) watchEvent).context()
        final Path path = parentPath.resolve(relativePath)

        if (kind == ENTRY_MODIFY && !Files.isDirectory(path)) {
            handleModify(path)
        }
    }

    private void handleModify(final Path path) {
        onFileChangeHandler.onChange(path)
    }

    private void register(Path path) {
        if (! Files.isDirectory(path)) {
            path = path.getParent()
        }

        if (pathByKey.values().contains(path)) {
            return
        }

        final WatchKey key = path.register(watchService, [ENTRY_MODIFY] as WatchEvent.Kind[],
                SensitivityWatchEventModifier.HIGH)
        pathByKey.put(key, path)

        ConsoleOutputs.out("watching: ", path)
    }

    private void registerDirs(Path rootPath) {
        register(rootPath)
        rootPath.traverse type: FileType.DIRECTORIES, visit: {
            register(it)
        }
    }

    private static WatchService createWatchService() {
        return FileSystems.getDefault().newWatchService()
    }
}
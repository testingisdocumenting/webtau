# File Content

:include-file: doc-artifacts/snippets/fsFileContent/createFile.groovy {
  title: "write content to a file",
  includeRegexp: ["fs\\.writeText"]
}

:include-file: doc-artifacts/snippets/fsFileContent/readFile.groovy {
  title: "validate file content",
  surroundedBy: "assert-file"
}

`fs.textContent` declares file content, but doesn't access it right away. 
WebTau reads file content when validation happens. Here is an example of waiting on file content:

:include-file: doc-artifacts/snippets/fsFileContent/readFile.groovy {
  title: "wait for file content",
  surroundedBy: "wait-for-id"
}

Use `.data` to access actual file content for further processing  

:include-file: doc-artifacts/snippets/fsFileContent/readFile.groovy {
  title: "access file content",
  surroundedBy: "actual-file-content"
}

Use `extractByRegexp` to extract content from a file by regular expression

:include-file: doc-artifacts/snippets/fsFileContent/readFile.groovy {
  title: "extract file content",
  surroundedBy: "extract-id",
  excludeRegexp: "statusCode"
}

Use `replaceText` to replace text in place

:include-file: doc-artifacts/snippets/fsFileContent/replaceFile.groovy {
  title: "replace file content",
  surroundedBy: "replace-text"
}

```columns
left: :include-file: doc-artifacts/fs-content-to-replace.txt {title: "file to replace"}
right: :include-file: doc-artifacts/fs-content-after-replace-text.txt {title: "result of replace"}
```

# Copy

:include-file: doc-artifacts/snippets/fsCopy/copyFileToDir.groovy {
  title: "copy single file to a directory",
  includeRegexp: ["fs\\.copy", "createDir"]
}

:include-file: doc-artifacts/snippets/fsCopy/copyFileToTempDir.groovy {
  title: "copy single file to a temp directory",
  includeRegexp: ["fs\\.copy", "tempDir"]
}

:include-file: doc-artifacts/snippets/fsCopy/copyFileToFile.groovy {
  title: "copy single file to a file",
  includeRegexp: ["fs\\.copy"]
}

# Archive

:include-file: scenarios/fs/archive.groovy {
  title: "zip a directory",
  surroundedBy: "// zip",
}

:include-file: scenarios/fs/archive.groovy {
  title: "unzip a file",
  surroundedBy: "// unzip",
}

:include-file: scenarios/fs/archive.groovy {
  title: "untar a file",
  surroundedBy: "// untar",
}

# Temporary Dirs And Files

:include-file: scenarios/fs/temp.groovy {
  title: "create temp directory",
  surroundedBy: "// temp-dir",
}

:include-file: scenarios/fs/temp.groovy {
  title: "create temp directory inside directory",
  surroundedBy: "// temp-dir-parent",
}

:include-file: scenarios/fs/temp.groovy {
  title: "create temp file",
  surroundedBy: "// temp-file",
}

:include-file: scenarios/fs/temp.groovy {
  title: "create temp file inside a directory",
  surroundedBy: "// temp-file-parent",
}

:include-file: doc-artifacts/temp-file-path.txt {
  title: "example of temp file location"
}


Note: temp files and temp directories will be automatically deleted at the end of run 
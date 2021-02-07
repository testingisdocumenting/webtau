# File Content

:include-file: doc-artifacts/snippets/fsFileContent/createFile.groovy {
  title: "write content to a file",
  includeRegexp: ["fs\\.writeText"]
}

:include-file: doc-artifacts/snippets/fsFileContent/readFile.groovy {
  title: "validate file content",
  startLine: "assert-file",
  endLine: "assert-file",
  excludeStartEnd: true
}

`fs.textContent` declares file content, but doesn't access it right away. 
Webtau reads file content when validation happens. Here is an example of waiting on file content:

:include-file: doc-artifacts/snippets/fsFileContent/readFile.groovy {
  title: "wait for file content",
  startLine: "wait-for-id",
  endLine: "wait-for-id",
  excludeStartEnd: true
}

Use `.data` to access actual file content for further processing  

:include-file: doc-artifacts/snippets/fsFileContent/readFile.groovy {
  title: "access file content",
  startLine: "actual-file-content",
  endLine: "actual-file-content",
  excludeStartEnd: true
}

Use `extractByRegexp` to extract content from a file by regular expression

:include-file: doc-artifacts/snippets/fsFileContent/readFile.groovy {
  title: "extract file content",
  startLine: "extract-id",
  endLine: "extract-id",
  excludeStartEnd: true,
  excludeRegexp: "statusCode"
}

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

# Unzip

:include-file: doc-artifacts/snippets/fsArchive/unzip.groovy {
  title: "unzip a file"
}





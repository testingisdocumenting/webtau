# Validation

:include-file: doc-artifacts/snippets/fsFileContent/readFile.groovy {
  title: "validate file content",
  surroundedBy: "assert-file"
}

# Content Extraction

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

# Write To File

:include-file: doc-artifacts/snippets/fsFileContent/createFile.groovy {
  title: "write content to a file",
  includeRegexp: ["fs\\.writeText"]
}

# Replace File Content

Use `replaceText` to replace text in place

:include-file: doc-artifacts/snippets/fsFileContent/replaceFile.groovy {
  title: "replace file content",
  surroundedBy: "replace-text"
}

```columns
left: :include-file: doc-artifacts/fs-content-to-replace.txt {title: "file to replace"}
right: :include-file: doc-artifacts/fs-content-after-replace-text.txt {title: "result of replace"}
```

# Wait For Content 

`fs.textContent` declares file content, but doesn't access it right away.
WebTau reads file content when validation happens. Here is an example of waiting on file content:

:include-file: doc-artifacts/snippets/fsFileContent/readFile.groovy {
  title: "wait for a specific file content",
  surroundedBy: "wait-for-id"
}

Use `takeSnapshot` and `waitTo change` to wait for any file change:

:include-file: doc-artifacts/snippets/fsFileContent/waitForChange.groovy {
  title: "wait for any content change",
  surroundedBy: ["file-take-snapshot", "file-wait-to-change"],
  surroundedBySeparator: "..."
}


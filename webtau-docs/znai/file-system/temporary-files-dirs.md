# Temporary Dirs

:include-file: scenarios/fs/temp.groovy {
  title: "create temp directory",
  surroundedBy: "// temp-dir",
}

:include-file: scenarios/fs/temp.groovy {
  title: "create temp directory inside directory",
  surroundedBy: "// temp-dir-parent",
}

# Temporary Files 

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

# Automatic Deletion

Note: temp files and temp directories will be automatically deleted at the end of run 

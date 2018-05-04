scenario ("""# first header
optionally split into multiple lines and has a header
""") {
    throw new RuntimeException("error on purpose")
}

scenario("""# second header
optionally split into multiple lines and has a header
""") {
    def a = 10
    a.should == 11
}

scenario("""# third header
optionally split into multiple lines and has a header
""") {
    println "hello"
}

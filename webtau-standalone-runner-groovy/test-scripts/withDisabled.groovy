dscenario('disabled test one') {
    throw new RuntimeException("will not be executed")
}

scenario('enabled test') {
    def a = 10
    a.should == 11
}

dscenario('disabled test two') {
    throw new RuntimeException("will not be executed")
}

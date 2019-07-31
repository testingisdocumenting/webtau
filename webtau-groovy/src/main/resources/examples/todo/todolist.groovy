import static com.twosigma.webtau.WebTauGroovyDsl.*

scenario('fetch todo item') {
    http.get('/todos/1') {
        title.should == 'delectus aut autem'
        completed.should == false
    }
}
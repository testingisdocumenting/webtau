search.submit('name')

browser.reopen("/search") // page is going to be refreshed
search.searchMessage.should == ''
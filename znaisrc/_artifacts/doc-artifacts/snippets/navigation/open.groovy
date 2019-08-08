search.submit('query')

browser.open("/search") // page is not be ing refreshed
search.searchMessage.should == 'searching for query'
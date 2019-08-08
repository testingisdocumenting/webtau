browser.open('/local-storage')
browser.localStorage.setItem('favoriteColor', 'pretty')

browser.refresh()
$('#favorite-color').should == 'pretty'

browser.restart()
$('#favorite-color').should == ''
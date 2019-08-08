browser.open('/resource-creation')

$('#new').click()
browser.url.ref.waitTo == 'created-id'
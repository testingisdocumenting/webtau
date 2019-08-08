def customFormElement = $('#answer')

customFormElement.setValue('hello')
customFormElement.should == 'hello'
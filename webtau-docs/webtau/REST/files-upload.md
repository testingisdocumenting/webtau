# File System Content

In following examples backend expects a file passed as `multipart/form-data`. File content is expected to be stored in `file` field.  
Backend responds with received file name and file description.  

To `POST` form data, you need to use the same `http.post` statement as you saw in previous examples. 
Second parameter should be `http.formData` instead of a map payload we used for `JSON`. 

:include-groovy: com/twosigma/webtau/http/HttpTest.groovy {entry: "file upload example simple", bodyOnly: true}

Use `http.formFile` to override file name that is being sent to the backend.

:include-groovy: com/twosigma/webtau/http/HttpTest.groovy {entry: "file upload example with file name override", bodyOnly: true}

You can specify additional to `file` form fields in the same request 

:include-groovy: com/twosigma/webtau/http/HttpTest.groovy {entry: "file upload example multiple fields", bodyOnly: true}

# In Memory Content

If your test already has content, you can explicitly pass it as is.

:include-groovy: com/twosigma/webtau/http/HttpTest.groovy {entry: "file upload example with in-memory content", bodyOnly: true}

Note: no file name is passed and this particular backend generated file name on your behalf.

Use `http.formFile` to provide a file name

:include-groovy: com/twosigma/webtau/http/HttpTest.groovy {entry: "file upload example with in-memory content and file name", bodyOnly: true}





# REST

0.function description:
  uri
  http method
  possible response status
  
1.display all files metadata:
  /api/file
  GET
  200, 404

2.display one file metadata with given name:
  /api/file/given-name
  GET
  200, 404
  
3.download one file with given name:
  /api/file/given-name/download
  GET
  200, 404, 500
  
4.upload a file:
  /api/file
  POST
  201, 409, 500

# History service

## Description
Microservice for working with history

## REST-services:
        
#### POST [http://localhost:9000/historyservice/history](http://localhost:9000/demo/history)
#### registration of a history

        Example of request body:
        {
          "userName": "Tom",
          "timestamp": "2019-06-01T10:15:39.476Z",
          "description": "some description"
        }

---

#### GET [http://localhost:9000/demo/history?pagesize={pagesize}&pagenumber={pagenumber}](http://localhost:9000/demo/history?pagesize={pagesize}&pagenumber={pagenumber})
#### receiving history

        where:
        {pagesize} - number of elements per page (optional parameter, default 15)
        {pagenumber} - page number (optional parameter, default 1)

---

#### GET [http://localhost:9000/demo/history/{historyId}](http://localhost:9000/demo/history/{historyId}})
#### receiving history by id

        where:
        {historyId} - identity number
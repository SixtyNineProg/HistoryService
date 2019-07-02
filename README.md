# History service

## Description
Microservice for working with history

## REST-services:

#### POST [http://localhost:9000/historyservice/history](http://localhost:9000/historyservice/history)
#### registration of a history

        Example of request body:
        {
          "userName": "Tom",
          "timestamp": "2019-04-30T10:15:39.476Z",
          "operatingType": "registration",
          "entityType": "company",
          "isWaslStatus": true,
          "isWialonStatus": true,
          "waslDescription": "description",
          "wialonDescription": "description",
          "entityDescription": "description"
        }

---

#### GET [http://localhost:9000/historyservice/history?pagesize={pagesize}&pagenumber={pagenumber}](http://localhost:9000/historyservice/history?pagesize={pagesize}&pagenumber={pagenumber})
#### receiving history

        where:
        {pagesize} - number of elements per page (optional parameter, default 15)
        {pagenumber} - page number (optional parameter, default 1)

---

#### GET [http://localhost:9000/historyservice/history/{historyId}](http://localhost:9000/historyservice/history/{historyId}})
#### receiving history by id

        where:
        {historyId} - identity number
        
---        
        
#### POST [http://localhost:9000/historyservice/history/filter](http://localhost:9000/historyservice/history/filter})
#### receiving filtered history

        Example of request body:
        {
          "userNames": ["Tom", "Jim"],
          "timestampFrom": "2019-04-01T10:15:39.476Z",
          "timestampTo": "2019-04-30T16:12:39.476Z",
          "operatingType": ["registration", "deletion"],
          "entityType": ["company", "vehicle", "driver"],
          "isWaslStatus": ["true"],
          "isWialonStatus": ["true"]
        }
# automs-ddg-weather-svc
kotlin ddg automation for automs


_request:_
``` 
http://localhost:8080/api/automation/run/
```
```json
{
    "automationResourceId": "soaresdev/automs-ddg-weather-svc",
    "orderId": "orderSample5543",
    "requestId": "sample-4624-9180-4ccda3cb5d73",
    "inputParams": {
        "city": "balneario camboriu",
        "geoHash": "6gjqmq"
    },
    "config": {
        "pageCopyConfig": {
            "storePage": false
        },
        "screenshotConfig": {
            "storeScreenshot": true,
            "target": "ELEMENT",
            "elementType": "XPATH",
            "withElement": "//div[@id='links_wrapper']/div[1]/div[3]/div/div[1]"
        },
        "chromeDriverOptionsConfig": {
            "sessionOptions": [
                "--user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4249.0 Safari/537.36"
            ]
        }
    }
}
```

_response:_
```json
{
    "processingTime": "2020-10-09T07:12:41.941Z",
    "orderId": "orderSample5543",
    "requestId": "sample-4624-9180-4ccda3cb5d73",
    "automationResourceId": "publisher/sample-automation",
    "inputParams": {
        "city": "balneario camboriu",
        "geoHash": "6gjqmq"
    },
    "config": {
        "pageCaptureConfig": {
            "enableScreenCapture": true,
            "captureType": "FULL_PAGE"
        },
        "pageCopyConfig": {
            "enablePageCopy": true,
            "enableStructuredResponseCopy": true,
            "usingCharset": "UTF8",
            "usingAbsolutLinks": true,
            "replacingAbsolutLinksOnElements": {
                "a": [
                    "href"
                ],
                "img": [
                    "src"
                ],
                "link": [
                    "href"
                ],
                "script": [
                    "src"
                ]
            }
        },
        "chromeDriverOptionsConfig": {
            "httpProxy": "none",
            "customUserAgent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36",
            "elementSearchTimeout": 5
        }
    },
    "response": {
        "processingStatus": "OK",
        "customResponse": "balneario camboriu weather/geo captured",
        "responseEntity": {
            "temperature": "18",
            "date": "Friday 7 AM",
            "sky": "Overcast",
            "wind": "12 KPH SE",
            "humidity": "80%",
            "coordinates": "-26.99066162109375 / -48.6309814453125"
        },
        "sessionFiles": [
            {
                "label": "udf",
                "contentType": {
                    "mimeType": "application/octet-stream",
                    "charset": "UTF-8"
                },
                "extension": "NONE"
            },
            {
                "label": "udf",
                "contentType": {
                    "mimeType": "application/octet-stream",
                    "charset": "UTF-8"
                },
                "extension": "JSON"
            }
        ]
    }
}
```



_attach to the app-automation bash:_
```
docker exec -t -i app-automation sh
```

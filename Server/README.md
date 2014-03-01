# Server 

## Endpoints
Endpoint followed by parameters (a default is specified if it's optional)

GET /api/images/:id  

GET /api/images/:id/raw  

GET /api/images  
- offset (default 0)  
- limit  (default 25)

## Parameters

Parameters are set by appending a question mark followed by key-values specified like so:  
key=value, separated by ampersands (&)  
Examples: /api/images?offset=25&limit=10  
/api/images?limit=50  

If a parameter is not set (and isn't required to be) the default value will be used.
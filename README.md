# DataGrinder
Data processing and transformation Restful services

#### Requirement
*  Java 8
*  Spring Boot 2.3.0
*  Maven

#### Build and Run
```
 ./mvnw spring-boot:run
```
---
#### Restful API Endpoints
/flatten : transform nested json to flat json.
```yaml
{
  "A_B_C_0_D": "E",
  "A_B_C_1_F": "G"
}
```

/summarize : transform multiple order records to a summary report for each user.
```yaml
{ 
  "886": {
        "info": {
            "id": 886,
            "name": "Alfred",
            "phone": "5210145236"
        },
        "order": {
            "2020-05-10": [
                {
                    "item": "Rubber",
                    "amount": 1,
                    "price": 30.24
                }
            ]
        },
        "totalAmount": 30.24
    }
}
```

/validate : validate and provide suggestion for invalid data.
```yaml
{
    "valid": false,
    "errors": [
        "Id must be positive integer",
        "Name must have both first name and last name in capitalization format and separated with a white space",
        "Phone number must contain only number with 10-digit long"
    ],
    "suggestion": {
        "id": 5,
        "name": "Valid Name",
        "phone": "0874512546"
    }
}
```

/sort : sort integer.
```yaml
{
    "data": [6, 25, 36, 50, 69, 88, 251, 845, 8745]
}
```

**Example json input for each api can be found [here](https://github.com/ManaAuea/DataGrinder/tree/master/src/example)**

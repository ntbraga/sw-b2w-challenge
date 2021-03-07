### B2W Star Wars Challenge

* JDK 11
* Maven 3.x
* Package as .war

* Package with:
`mvn clean package`

* Run with:
`mvn spring-boot:run`

* If you are using Intellij Community and cannot debug, please use
`-Dspring-boot.run.fork=false`.

* Test with:
`mvn test`

By default will run on port `3000` under `/api/v1`;

####Planet resource

Create planet: `POST /api/v1/planets/{id}`

Eg.
``{
"name": "Tatooine",
"climate": "Árido",
"terrain": "Deserto"
}``

* All fields are required

Update planet: `PUT /api/v1/planets/{id}`

Eg.\
`GET /api/v1/planets/6045304ff26e24482deddbf5`\
``{
"name": "Alderaan"
}``

* Updates name and movie appearances
* Empty fields will result on `400 BAD REQUEST`
* `null` fields will not be considered

List all planets: `GET /api/v1/planets`\
Search by planet name: `GET /api/v1/planets?name=Tatooine`

* Paging and Sorting are available through Spring Api\
  https://docs.spring.io/spring-data/rest/docs/2.4.x/reference/html/#paging-and-sorting
  
Find by id: `GET /api/v1/planets/{id}`\
Delete by id: `DELETE /api/v1/planets/{id}`



import org.springframework.cloud.contract.spec.Contract

Contract.make {
  description("When a GET request with two dates is made, the List of Reservation is returned")
  request {
    method 'GET'
    url '/api/v1/reservations/2021-06-21/2021-06-22'
  }
 response {
   status OK()
   body("""
         {
		 "data":[
                   {
                    "hotelId": "1",
                     "roomIds": [
                                 1,
                                 2,
                                 3
                                ]
                    }
                ],
				"status":"OK"
				}
  """)
    headers {
      contentType(applicationJson())
    }
  }
}
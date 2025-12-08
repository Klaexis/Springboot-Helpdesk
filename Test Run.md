***<---------- INITIALIZE DUMMY DATA ---------->***

Load/Initialize Data (POST)

**http://localhost:8080/database/load**



***<---------- EMPLOYEE POSITIONS ---------->***

Get all positions (Admin Only) (GET)

**http://localhost:8080/admin/{adminId}/positions**

**http://localhost:8080/admin/1/positions**



Get a specified position based on ID (Admin Only) (GET)

**http://localhost:8080/admin/{adminId}/positions/get/{positionId}**

**http://localhost:8080/admin/1/positions/get/2**



Create a position (Admin Only) (POST) (TEXT)

**http://localhost:8080/admin/{adminId}/positions/create**

**http://localhost:8080/admin/1/positions/create**

Database Administrator

Data Engineer



Update a position (Admin Only) (PATCH) (TEXT)

**http://localhost:8080/admin/{adminId}/positions/update/{positionId}**

**http://localhost:8080/admin/1/positions/update/4**

Software Engineer



Delete a position (Admin Only) (DELETE)

**http://localhost:8080/admin/{adminId}/positions/delete/{positionId}**

**http://localhost:8080/admin/1/positions/delete/3**





***<---------- ADMIN FUNCTIONS ---------->***

Get all Employees (Admin Only) (GET)

**http://localhost:8080/admin/{adminId}/employees**

**http://localhost:8080/admin/1/employees**



Get an employee based on ID (Admin Only) (GET)

**http://localhost:8080/admin/{adminId}/employees/find/{employeeId}**

**http://localhost:8080/admin/1/employees/find/2**



Create an employee (Admin Only) (POST) (JSON)

**http://localhost:8080/admin/{adminId}/employees/create**

**http://localhost:8080/admin/1/employees/create**

{

  "employeeName": "John Doe",

  "employeeAge": 30,

  "employeeAddress": "123 Main St, Cityville",

  "employeeContactNumber": "555-1234",

  "employeeEmail": "john.doe@example.com",

  "employmentStatus": "ACTIVE",

  "positionTitle": "Software Engineer"

}



{

    "employeeName": "Jane Smith",

    "employeeAge": 28,

    "employeeAddress": "456 Elm St, Townsville",

    "employeeContactNumber": "555-5678",

    "employeeEmail": "jane.smith@example.com",

    "employmentStatus": "ACTIVE",

    "positionTitle": "Admin"

}



Update an employee (Admin Only) (PATCH) (JSON)

**http://localhost:8080/admin/{adminId}/employees/update/{employeeId}**

**http://localhost:8080/admin/1/employees/update/3**

{

    "employeeName": "John McDoenalds",

    "employeeAddress": "456 Second St, New City",

    "employeeContactNumber": "123-4567",

    "employeeEmail": "john.mcdoenalds@example.com"

}



Delete an employee (Admin Only) (DELETE)

**http://localhost:8080/admin/{adminId}/employees/delete/{employeeId}**

**http://localhost:8080/admin/1/employees/delete/4**



Assign position to an employee (Admin Only) (PATCH) (TEXT)

**http://localhost:8080/admin/{adminId}/employees/assign-position/{employeeId}**

**http://localhost:8080/admin/1/employees/assign-position/3**

**Admin**





***<---------- ADMIN TICKET FUNCTIONS ---------->***

File a ticket (POST) (JSON)

**http://localhost:8080/employee/{employeeId}/tickets/file**

**http://localhost:8080/employee/3/tickets/file**

{

    "ticketTitle": "Fix Bugs",

    "ticketBody": "Fix bugs in project A"

}

**http://localhost:8080/employee/2/tickets/file**

{

    "ticketTitle": "Add Feature",

    "ticketBody": "Add new feature in project B"

}



Get all tickets (Admin Only) (GET)

**http://localhost:8080/admin/{adminId}/tickets**

**http://localhost:8080/admin/1/tickets**



Get a ticket (Admin Only) (GET)

**http://localhost:8080/admin/{adminId}/tickets/find/{ticketId}**

**http://localhost:8080/admin/1/tickets/find/1**



Assign ticket (Admin Only) (PATCH)

**http://localhost:8080/admin/{adminId}/tickets/assign/{ticketId}/assignTo/{employeeId}**

**http://localhost:8080/admin/1/tickets/assign/2/assignTo/3**

**http://localhost:8080/admin/1/tickets/assign/1/assignTo/2**



Update ticket (Admin Only) (PATCH)

**http://localhost:8080/admin/{adminId}/tickets/update/{ticketId}**

**http://localhost:8080/admin/1/tickets/update/1**

{

    "ticketTitle": "\[URGENT] Fix Bugs",

    "ticketBody": "Fix Bugs in project A ASAP"

}



Update ticket status (Admin Only) (PATCH) (TEXT)

**http://localhost:8080/admin/{adminId}/tickets/update/{ticketId}/status**

**http://localhost:8080/admin/1/tickets/update/2/status**

"IN\_PROGRESS"



Add Remarks for Admin (Admin Only) (PATCH) (JSON)

**http://localhost:8080/admin/{adminId}/tickets/addRemarks/{ticketId}**

**http://localhost:8080/admin/1/tickets/addRemarks/1**

{

  "remark": "Employee is fixing the bugs",

  "newStatus": "IN\_PROGRESS"

}

**http://localhost:8080/admin/1/tickets/addRemarks/2**

{

  "remark": "This ticket is now in review"

}



***<---------- EMPLOYEE TICKET FUNCTIONS ---------->***

Get all assigned tickets (PATCH)

**http://localhost:8080/employee/{employeeId}/tickets/get/assignedTickets**

**http://localhost:8080/employee/3/tickets/get/assignedTickets**



Update own filed ticket (PATCH) (JSON)

**http://localhost:8080/employee/{employeeId}/tickets/update/{ticketId}**

**http://localhost:8080/employee/2/tickets/update/2**

{

    "ticketTitle": "\[URGENT] Add features",

    "ticketBody": "Add Features in project B ASAP"

}



Update assigned ticket status (PATCH) (TEXT)

**http://localhost:8080/employee/{employeeId}/tickets/updateStatus/{ticketId}**

**http://localhost:8080/employee/3/tickets/updateStatus/2**

"IN\_PROGRESS"

**http://localhost:8080/employee/2/tickets/updateStatus/1**

"FILED"



Add remarks to assigned ticket (PATCH) (JSON)

**http://localhost:8080/employee/{employeeId}/tickets/addRemarks/{ticketId}**

**http://localhost:8080/employee/3/tickets/addRemarks/2**

{

  "remark": "Adding new Features",

  "newStatus": "IN\_PROGRESS"

}

**http://localhost:8080/employee/2/tickets/addRemarks/1**

{

  "remark": "Currently fixing the bugs"

}



Get all filed tickets (GET)

**http://localhost:8080/employee/{employeeId}/tickets/get/filedTickets**

**http://localhost:8080/employee/2/tickets/get/filedTickets**



Get a filed ticket (GET)

**http://localhost:8080/employee/{employeeId}/tickets/get/filedTicket/{ticketId}**

**http://localhost:8080/employee/2/tickets/get/filedTicket/1**


# SDA7

Vaccine recipient:
Username: jmccleary
Password: pw

Vaccine recipients may search for timeslots by Area and HCP, as well as view their 
vaccination certificates.

Each HCP must have an area code associated with its record. When a recipient searches, the 
database pulls all records associated with a provider with the given area code.

Similarly, a person can search for a given Healthcare Provider by searching their name.

To book, simply click on the name of an available timeslot. You will be taken to a questionnaire.
If you pass the questionnaire, the booking is automatically made and should no longer show up
in subsequent searches.

The database is populated with 2 Healthcare Providers with timeslots. 

Name: 'Royal Melbourne Hospital'
Area code: '3051'

Name: 'The Alfred'
Area code: '3004'

The view certificate button shows the date that the vaccine was received.

Health Care Provider
Username: Royal Melbourne Hospital
Password: pw

Healthcare providers can add timeslots by selecting a start time, end time, and 
vaccine type that matches one in the database.'Pfizer' and 'AstraZeneca' are valid options. 
If the dates are after 6 months or endate is before start date, throws warning.

Add vaccination record displays a list of all timeslots with a recipientid. Our intent is
that once an appointment is booked, the provider can confirm it at any point, in case
the person shows up early.

Admin
Username: TestAdmin
Password: pw

Adding an account expects a type of either HCP or Recipient and should give a warning if
not included. Birthdate is required for recipients, postcode for HCPs.

Adding Vaccine Type takes any string.

View all users shows a list of all users and their role.

View all timeslots shows all time slots.

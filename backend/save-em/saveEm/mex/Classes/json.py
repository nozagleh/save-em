class GenerateJSON:

	def getPersonJSON(self, person):
		data = {}
		data['id'] = person.id
		data['firstname'] = person.firstname
		data['lastname'] = person.lastname
		data['birthday'] = person.birthdate

		# TODO Create person.getSex() function
		if person.sex == 0:
			gender = "Female"
		elif person.sex == 1:
			gender = "Female"
		data['gender'] = gender

		data['nationality'] = person.nationality
		data['height'] = person.height
		data['haircolor'] = person.haircolor
		data['weight'] = person.weight
		data['timeMissing'] = person.timeofmissing
		data['placeMissing'] = person.placeofmissing
		data['countryMissing'] = person.countryofmissing
		data['found'] = person.found
		data['remarks'] = person.comments

		return data

	def getAllPersonsJSON(self, *persons):
		data = {}
		count = 0
		for person in persons:
			data[person.id] = self.getPersonJSON(person)
			count+1

		return data
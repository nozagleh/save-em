from ..models import PersonImages;

class GenerateJSON:

	def getPersonJSON(self, person, onlyId):
		data = {}
		data['id'] = person.id

		if onlyId:
			return data

		data['firstname'] = person.firstname
		data['lastname'] = person.lastname
		data['birthday'] = person.birthdate
		data['gender'] = person.sex
		data['nationality'] = person.nationality
		data['height'] = person.height
		data['haircolor'] = person.haircolor
		data['weight'] = person.weight
		data['missingDate'] = person.missingdate
		data['gpsLocation'] = person.gpslocation
		data['found'] = person.found
		data['remarks'] = person.comments
		img = PersonImages.objects.filter(person=person.id).exists()

		if img == True:
			img = PersonImages.objects.get(person=person.id)
			data['img_url'] = img.image.url

		return data

	def getAllPersonsJSON(self, *persons):
		data = {}
		count = 0
		dictPersons = {}
		for person in persons:
			dictPersons.update({count:self.getPersonJSON(person, False)})
			count = count+1

		data = {'persons':dictPersons}
		return data
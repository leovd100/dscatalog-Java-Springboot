import phonenumbers
from phonenumbers import geocoder
import opencage
import folium
number = "+55" + str(input("digite o número: "))

pepnumber = phonenumbers.parse(number)
location = geocoder.description_for_number(pepnumber, "en")
print("localtion: ", location)

from phonenumbers import carrier

service_pro = phonenumbers.parse(number)
print(carrier.name_for_number(service_pro, "en"))

from opencage.geocoder import OpenCageGeocode

key = 'a1db4fe402264d6ca696db5e0f38cf52'

geocoder = OpenCageGeocode(key)
query = str(location)
result = geocoder.geocode(query)
print(result)
lat = result[0]['geometry']['lat']
lng = result[0]['geometry']['lng']

print(lat, lng)


myMap = folium.Map(location=[lat, lng], zoom_start=9)
folium.Marker([lat,lng], popup=location).add_to(myMap)

myMap.save("mylocation.html")
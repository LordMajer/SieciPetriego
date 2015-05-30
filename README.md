# SieciPetriego
Projekt- metody formalne

Parametry z konsultacji:
- ograniczonoć miejsc (k ograniczonoć)
- zachowawczoć
- zachowawczoć wzgldem wektora wag (minimalny wektor wag)
- odwracalnoć sieci
- żywotnoć sieci- żywotnoć przejć.
- 
- Co do algorytmu do analizy odwracalnoci... W odwracalnoci chodzi chyba o to że jeli jestemy w miejscu M1 i dotrzemy do miejsca M2 to czy możemy wrócić z miejsca M2 do M1. Sposobem może być przejscie petla po każdym miejscu. Dla tego miejsca tworzymy liste miejsc osigalnych a potem dla każdego z tych miejsc z listy również tworzymy taka liste osiagalnoci i sprawdzamy czy znajduje sie na niej miejsce źródowe. Jeli tak to ok a jak nie to znaczy że nie jest odwracalny?
- 
Przykad: sprawdzamy miejsce M1
wierzcholki osiagalne to np M2,M3,M4
dla M2 tworzymy liste osiagalnosci tak jakbysmy startowali od niego i sprawdzamy czy na tej liscie znajduje sie M1 jeli tak to odwracalnoć zachowana jeli nie ma go to jest niezachowana. Tak samo dla M3 i M4.

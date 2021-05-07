# Izsakazsivany
Roguelike játék a Binding of Isaac sorozat által inspirálva.

Fontos tudnivalók a játékkal kapcsolatban:
- A kezdő szoba egyik szomszédja az Item Room (arany ajtókeret), itt találhatunk kettő tárgyat, az egyik megnöveli a statisztikákat, és ebből bármennyit felvehetünk egy menet során (statitem); a másik egy fegyver, ami nagyobb jelentősebb mértékben növeli a statisztikákat, bár nem mindet, és egyszerre csak egy fegyvert lehet használni, ha másikat veszünk fel helyette, akkor a jelenlegi el lesz dobva.
- Tárgyakat a B gomb megnyomásával lehet felvenni.
- A kezdő szobától második legtávolabbi szoba a Bolt, ahol véletlen számú és típusú tárgyakat lehet vásárolni, ezek stat itemek, fegyverek, vagy bájitalok.
- Bájitalból két féle van, az egyik rögtön tapasztalati pontot ad, a másikat eltárolja a karakter, és a Q gomb megnyomásával lehet felhasználni, ezekből több is lehet egyszerre nálunk és egyszerre csak egyet használ.
- Az összes tárgy kijelzi maga mellett a statisztikáit, ezek véletlen generáltak, és ahogy haladunk pályáról pályára egyre mélyebbre, úgy egyre jobban növekszenek majd a statiszikák.
- A boltban a tárgyak árai a tárgy értékétől függően vannak számolva, és ahogy újabb pályákra jutunk, úgy nő a tárgyak ára.
- Pénzt ellenségek legyőzésével szerezhetünk, de ezen kívül tapasztalati pontokat is kapunk, és ritkán valamilyen tárgyat is akár.
- A tapasztalati pontok segítségével tudunk szintet lépni, minden szintlépéskor 20-szal nő a maximális életerő, és teljesen visszatölt az életerő.
- Mozogni a WASD billentyűkkel lehet, akár átlósan is.
- Támadni pedig a nyilakkal lehet, mozgástól független irányba, és akár átlósan is.
- Lehetőség van a térkép megnyitására az M billentyűvel, ami addig meg is állítja a játékot. Ezen a képernyőn az aktuális pálya térkép mellett a statisztikáinkat, (bal oldalt), a tárolt bájitaljainkat (felül), a jelen fegyverünk statisztikáit (jobb oldalt) és a felvett stat itemjeink ikonjait (lent) is láthatjuk. A térképen a P karakter jelöli a játékos pozícióját, S jelöli a boltot, I az itemroomot, C a számos combat roomot és B a főellenség szobáját.
- Az egyes pályák véletlenszerűen generáltak, és minél mélyebbre megyünk, annál nagyobb lesz az pálya teljes mérete.
- Új pályára a főellenség legyőzése után a csapóajtón keresztül mehetünk, megelőző pályá(k)ra már nem lehet visszamenni.
- A játék hangerejét is lehet állítani, ehhez az escape-et kell megnyomnunk, majd ott a slideren beállítani a megfelelő értéket.
- Egy harci szoba ránk zárul, ha belépünk, és csak akkor nyílnak ki az ajtók, ha az összes ellenfelet legyőztük. Amint ez megtörtént, utána már nem fognak újraéledni abban a szobában az ellenségek, tehát egy  pályán csak limitált mennyiségű pénzt és tapasztalati pontot szerezhetünk.


A választott keretrendszer (java swing) egyszerűségéből fakadóan nem biztos, hogy minden számítógépen akadály nélkül fut majd a játék.  
A minimum gépigény nagyjából:
- GTX 1050 vagy jobb videókártya
- intel i5/i7-4000 széria vagy jobb processzor
- 4GB ram
Feltehetőleg ennél rosszabb hardveren is futhat megfelelően, pontosan nem tudjuk.


A source kódból generált .jar fájl elérhető az actions fül alatt. A "potention fix to yml" és az azt követő workflow-knál már külön lehet letölteni az artifact egyes részeit.

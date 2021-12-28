# Podstawowe informacje
[Treść projektu](https://github.com/apohllo/obiektowe-lab) 

Autor: Michał Grela\
Grupa: 15, czwartek godz. 14:40 - 16:10, Informatyka - Data Science\
Udało się zaimplementować wszystkie wymagania projektu

# Uruchamianie
Linux - `./gradlew run` \
Windows - `.\gradlew.bat run`

# Opis działania
## Menu startu
Poczatkowo użytkownikowi pokazuje się **menu startu** umożliwiające zmianę parametrów map.
Widoczne na obrazku wartości, są ustawione domyślnie. \
![](/images/mainMenu.png)

Po podaniu nieprawidłowych danych użytkownik jest informowany 
o konieczności podania danych w prawidłowym formacie przez **alert**. \
![](/images/invalidData.png)

## Widok główny
![](/images/mainView.png)
W widoku głównym po lewej stronie wyświetlana jest mapa z "murem" uniemożliwiajacym
wychodzenie zwierząt poza mapę. Po prawej stronie wyświetlana jest mapa zawinięta, która
sprawia, że w momencie wyjścia zwierzęcia poza mapę, przechodzi ono na jej drugą stronę.
Elementy na mapie oznaczone są kolorami w następujacy sposób:

* Dżungla - ![#006400](https://via.placeholder.com/15/006400/000000?text=+) 
* Step - ![#FFFF66](https://via.placeholder.com/15/FFFF66/000000?text=+) 
* Roślina - ![#90EE90](https://via.placeholder.com/15/90EE90/000000?text=+) 
* Zwierzę w zależnosci od obecnej energii:
  * \>= energia startowa - ![#FF2000](https://via.placeholder.com/15/FF2000/000000?text=+)
  * \>= 75% energii startowej - ![#FF6700](https://via.placeholder.com/15/FF6700/000000?text=+)
  * \>= 50% energii startowej - ![#FF9248](https://via.placeholder.com/15/FF9248/000000?text=+)
  * \>= 25% energii startowej - ![#FF9248](https://via.placeholder.com/15/FF9248/000000?text=+)
  * \> 0 - ![#FFD7B5](https://via.placeholder.com/15/FFD7B5/000000?text=+)
  * = 0 - ![#FFFFFF](https://via.placeholder.com/15/FFFFFF/000000?text=+)

Po lewej stronie mapy wyświetlany jest wykres statystyk w czasie, w celu zmiany
wyświetlanego wykresu należy wcisnąć przycisk z interesującą użytkownika statystyką.
Dla czytelności wykres pokazuje tylko 15 ostatnich epok.
Pod przyciskami do zmiany wykresu wyświetlany jest dominujący genotyp.

Do zatrzymania symulacji na danej mapie służy przycisk - "||" pod mapą.

## Widok po zatrzymaniu
![](/images/afterPause.png)
Symulacje można zatrzymywać niezależnie od siebie. W momencie zatrzymania przycisk pauzy zmienia się
na przycisk ponownego wznowienia symulacjie - ">". Oprócz tego pojawiają się dwa dodatkowe przyciski.
Jeden do wyświetlenia zwierząt z dominującym genotypem, drugi do zapisywania statystyk symulacji do pliku CSV.
Po wciśnieciu przycisku wyświetlenia zwierząt z dominującym genotypem. W osobnym oknie wyświetlana jest lista zwierząt 
zawierająca informacje o ich pozycji, energii oraz wieku.

![](/images/dominantGenotype.png)

Po zatrzymaniu animacji możliwe jest również wybranie zwierzęcia klikając odpowiednią komórkę na mapie.
Po wybraniu zwierzęcia w nowym oknie rozoczyna się jego śledzenie. Niemożliwe jest śledznie dwóch zwierząt na raz 
na jednej mapie. Aby śledzić innę zwierzę na tej samej mapie, należy najpierw zamknąć okno z śledzonym zwierzęciem.

![](/images/trackingAnimal.png)

Po ponownym uruchomieniu animacji, wartości śledzonego zwierzęcia aktualizują się na bieżąco.

![](/images/updatedTracking.png)

## Magiczna ewolucja
W przypadku wybrania magicznej zasady ewolucj, gdy do niej dojdzie użytkownik otrzymuje powiadomienie
o zaistniałej sytuacji wraz z informacją na której mapie doszło do magicznej ewolucji.

![](/images/magicEvolution.png)


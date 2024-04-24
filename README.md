<b>Instrukcja uruchomienia:</b>
<ol>
<li>Projekt należy sklonować z wymienionego wyżej repozytorium.</li>
<li>W Mavenie uruchomić cykl install, żeby powstał wynikowy plik .jar w katalogu target.</li>
<li>Będąc w głównym katalogu projektu stworzyć obraz z pliku Dockerfile poleceniem: 'docker build -t movieteka .'</li>
<li>Następnie utworzyć kontener składający się z aplikacji oraz bazy danych poleceniem: 'docker-compose up -d'</li>
</ol>
<b>Aplikacja jest gotowa do działania.</b>

<b>Profile aplikacji:</b>
<ul>
<li>default - budowanie obrazu wynikowego (aplikacja + baza)</li> 
<li>dev - profil developerski</li> 
<li>test - profil do przeprowadzania testów</li> 
</ul>

W przypadku chęci uruchomienia testów lub uruchomienia aplikacji z profilu dev wymagane jest wcześniejsze zbudowanie
kontenera z bazą danych z katalogu docker-local poleceniem 'docker-compose up -d'



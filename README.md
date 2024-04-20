Instrukcja uruchomienia:
Projekt należy sklonować z wymienionego wyżej repozytorium.
W Mavenie uruchomić cykl install, żeby powstał wynikowy plik .jar w katalogu target.
Będąc w głównym katalogu projektu stworzyć obraz z pliku Dockerfile poleceniem: 'docker build -t movieteka .'
Następnie utworzyć kontener składający się z aplikacji oraz bazy danych poleceniem: 'docker-compose up -d'
Aplikacja jest gotowa do działania.



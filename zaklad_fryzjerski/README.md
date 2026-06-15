## ZAŁOŻENIA SALONU FRYZJERSKIEGO
Zakład fryzjerski realizuje N rodzajów usług (np.: strzyżenie, modelowanie, golenie, …) i obsługuje klientów przybywających do niego w losowych odstępach czasu.
Klienci trafiają najpierw do poczekalni o ustalonej pojemności i czekają na wykonanie usługi wybranego rodzaju.
W zakładzie pracuje P fryzjerów, którzy są wyspecjalizowani w określonych usługach:
- P1 – liczba fryzjerów wyspecjalizowanych w strzyżeniu,
- P2 – liczba fryzjerów wyspecjalizowanych w modelowaniu,
- P3 – liczba fryzjerów wyspecjalizowanych w goleniu,
  
(P1, P2, P3, … <= P). Do dyspozycji fryzjerów jest L foteli (L < P).

## OPIS SYSTEMU
1. Kluczowe Komponenty Systemu

    * BarberShop (Salon): Centralny punkt synchronizacji. Zarządza:
        * Poczekalnią (waitingRoom): Listą klientów o ograniczonej pojemności.
        * Fotelami fryzjerskimi: Ich liczba jest mniejsza niż liczba fryzjerów, co wymusza rywalizację fryzjerów o dostęp do stanowiska pracy.
        * Mechanizmami blokad (ReentrantLock, Condition): Kontrolują dostęp do zasobów i usypiają wątki (fryzjerów czekających na klientów lub fotele oraz
          klientów czekających na wolne miejsce w poczekalni).
    * Barber (Fryzjer - wątek):
        * Każdy fryzjer posiada zestaw specjalizacji (identyfikatory usług, które potrafi wykonać).
        * Działa w pętli: czeka na moment, w którym dostępny będzie jednocześnie klient wymagający jego usługi ORAZ wolny fotel (pobiera oba zasoby atomowo),
          następnie wykonuje usługę (używając Thread.sleep) i zwalnia fotel.
    * Client (Klient - wątek):
        * Reprezentuje osobę przychodzącą do salonu z konkretnym żądaniem usługi.
        * Jeśli w poczekalni jest miejsce, wchodzi do niej i czeka, aż odpowiedni fryzjer go wybierze. Jeśli poczekalnia jest pełna – odchodzi (ten mechanizm
          można zaobserwować w enterWaitingRoom).
    * ClientGenerator (Generator - wątek):
        * W losowych odstępach czasu tworzy nowe obiekty klasy Client z losowo przypisanymi rodzajami usług.
    * SimulationManager:
        * Orkiestrator, który inicjalizuje salon, tworzy fryzjerów z odpowiednimi specjalizacjami i uruchamia wszystkie wątki.

2. Logika Synchronizacji (Model Wielowątkowy)

Projekt rozwiązuje kilka problemów współbieżności:

1. Atomowe Pobieranie Zasobów: Fryzjer w metodzie getNextClientAndChairForBarber czeka na moment, w którym dostępny jest zarówno odpowiedni klient, jak i wolny fotel.
   Zasoby te są zajmowane w jednej operacji chronionej zamkiem, co zapobiega błędom wizualnym (np. klient "znika" z poczekalni, ale nie ma dla niego fotela).
2. Bezpieczeństwo Danych: Dostęp do listy oczekujących i tablicy zajętości foteli jest chroniony wspólnym obiektem Lock, co zapobiega błędom typu race
   condition (np. dwóch fryzjerów biorących tego samego klienta).
3. Powiadomienia (Condition): Użycie stateChanged.signalAll() budzi fryzjerów za każdym razem, gdy w poczekalni pojawi się nowy klient lub zwolni się fotel.

3. Warstwa Wizualna (JavaFX)

Klasa HelloController pełni rolę obserwatora (SimulationObserver). Dzięki temu model matematyczny jest oddzielony od grafiki:

* Poczekalnia: Wizualizowana jako prostokąt, w którym pojawiają się kółka symbolizujące klientów.
* Fotele: Okręgi w centralnej części. Animacje (TranslateTransition) pokazują ruch fryzjera i klienta do fotela w momencie rozpoczęcia usługi.
* Specjalizacje: Nad kółkami fryzjerów i klientów widnieją oznaczenia (np. "P1, P2"), co pozwala na bieżąco śledzić, dlaczego dany fryzjer bierze
  konkretnego klienta.

4. Przepływ Symulacji

1. Użytkownik podaje parametry: liczbę usług (N), liczbę fryzjerów (P), liczbę foteli (L) oraz pojemność poczekalni.
2. Generator tworzy klientów. Klient wchodzi do poczekalni (waitingRoom.add).
3. Fryzjer czeka na wolny fotel oraz klienta ze swojej branży. Gdy oba zasoby są dostępne, "zabiera" klienta i zajmuje fotel w jednej operacji.
4. Następuje animacja "strzyżenia" (ruch fryzjera i klienta do wybranego fotela).
5. Po losowym czasie (3-7 sekund) fryzjer zwalnia fotel i klienta, po czym wraca do szukania kolejnego zadania.
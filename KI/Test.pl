contains(X,[X|_]).
contains(X, [ _ |Tail] ) :- contains(X,Tail).

count([],0).
count( [_|Tail] , X):- count(Tail, XT) , X is XT + 1.

count(_X,[],0).
count(X,[X|T],Y):- count(X,T,Z), Y is Z + 1.
count(X,[X1|T],Z):- X1 \= X, count(X,T,Z).

not_intersect(L1,L2):- \+ (contains(E,L1),contains(E,L2)).

unartig(susi).
unartig(fridolin).
unartig(ralf).

artig(hanna).
artig(britta).

sehr_artig(klaus).
sehr_artig(sina).

sterne(schaukelpferd, 100).
sterne(kreisel, 20).
sterne(buntstifte, 40).
sterne(teddybär, 60).
sterne(ritterburg, 110).
sterne(bausteine, 90).
sterne(buch, 40).
sterne(fahrrad, 200).
sterne(gameboy, 250).
sterne(puppe, 140).
sterne(murmeln, 30).

wunschzettel(susi, [schaukelpferd,teddybär,ritterburg,buch,puppe,teddybär,schaukelpferd]).
wunschzettel(fridolin, [ritterburg,ritterburg,bausteine,buch,buch,murmeln,puppe]).
wunschzettel(ralf, [murmeln,buch,kreisel,fahrrad]).
wunschzettel(hanna, [schaukelpferd,bausteine,fahrrad,fahrrad,bausteine,schaukelpferd]).
wunschzettel(britta, [gameboy,ritterburg,buntstifte,buch,puppe,murmeln]).
wunschzettel(klaus, [teddybär,ritterburg,fahrrad,puppe,murmeln,bausteine,murmeln]).
wunschzettel(sina, [gameboy,ritterburg,buntstifte,buch,puppe,murmeln]).

wunschwert([], 0).
wunschwert([Head|Tail], X):- wunschwert(Tail, XT) , sterne(Head, XS) , X is XT + XS.

wunschliste_bereinigen([],[]).
wunschliste_bereinigen([Head|Tail],Liste):- contains(Head,Tail) , wunschliste_bereinigen(Tail,Liste).
wunschliste_bereinigen([Head|Tail],[Head|Tail2]):- \+contains(Head,Tail) , wunschliste_bereinigen(Tail, Tail2).

erhalten_sterne(Kind,Sterne):- unartig(Kind) , Sterne is 100.
erhalten_sterne(Kind,Sterne):- artig(Kind) , Sterne is 200.
erhalten_sterne(Kind,Sterne):- sehr_artig(Kind) , Sterne is 300.

moegliche_geschenke(_,[]).
moegliche_geschenke(Kind,Liste):-
	erhalten_sterne(Kind,Sterne),
	wunschwert(Liste,SListe),
	SListe =< Sterne,
	
	wunschzettel(Kind,Zettel),
	forall(contains(E,Liste), (count(E,Liste,C), count(E,Zettel,Max), C =< Max)).
	
einzigartige_geschenke(Kind1, GeschenkListe1, Kind2, GeschenkListe2):-
	moegliche_geschenke(Kind1,GeschenkListe1),
	moegliche_geschenke(Kind2,GeschenkListe2),
	not_intersect(GeschenkListe1,GeschenkListe2).
	
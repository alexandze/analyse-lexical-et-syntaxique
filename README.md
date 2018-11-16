# analyse-lexical-et-syntaxique
Analyse lexical et syntaxique

On considère le langage L décrit par la grammaire G suivante:

<procedure> : := Procedure <identificateur> 1<déclarations>
  <instructions_affectation>
 Fin_Procedure <identificateur>2

<déclarations> : := <déclaration> <déclarations> | <déclaration>
<déclaration> : := declare <variable> : <type> ;
<variable> : := <identificateur>
<type> : := entier | reel
<instructions_affectation> : := <instruction_affectation> {; <instruction_affectation> }
<instruction_affectation> : := <variable> = <expression_arithmétique>
<expression_arithmétique> : := <terme> {(+ | -) <terme>}
<terme> : := <facteur> {(* | /) <facteur>}
<facteur> : := <variable> | <nombre> | (<expression_arithmétique>)

Nous considérons aussi les spécifications supplémentaires suivantes:
- (1) Un identificateur est une succession de lettres et de chiffres commençant par une lettre (ne
dépassant pas 8 caractères).
- (2) Un nombre est une suite de un ou plusieurs chiffres.
Construire, suivant la méthode de l’analyse descendante récursive, l’analyseur syntaxique (de façon
modulaire, avec l’analyseur lexical correspondant) du langage L décrit par la grammaire G. Implémentez
également les règles de sémantique statique suivantes1
 :
- (1) Les deux identificateurs 1 et 2 (mentionnés dans la déclaration d’une procédure) doivent être
identiques. Ils doivent aussi être différents des mots réservés du langage.
- (2) Toute variable utilisée dans une expression doit être préalablement déclarée.
- (3) On ne peut pas affecter le résultat d’une expression réelle (type reel) à une variable déclarée en
entier (type entier).
L’analyseur syntaxique aura en entrée un programme P (du langage L décrit par la grammaire G) dans un
fichier (.txt). Par ailleurs, l’analyseur devra être capable de mentionner le type d’erreur rencontrée

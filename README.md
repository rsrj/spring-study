# Spring-Study
Repositório para salvar toda a etapa de aprendizado de Spring.
 
Na pasta mysql-study foi reproduzido o tutorial para acessar o banco de dados do mySQL usando SpringBoot, disponível em: https://spring.io/guides/gs/accessing-data-mysql/

Na pasta springboot2-essentials foi implementado o tutorial do DevDojo, disponível em: https://www.youtube.com/playlist?list=PL62G310vn6nFBIxp6ZwGnm8xMcGE3VA5H 

# Instructions
## DevDojo
A partir da aula 30 do SB2-Essentials aborda-se o tema de testes.

Os testes para o repositório foram feitos com o DataJPA.

Os testes unitários para a regras de negócios do AnimeService e AnimeController foram feitos utilizando o JUnit 5 juntamente com o Mockito, o que permite a imitação do comportamento de cada método da classe sem a necessidade de realizar a integração. Para ambos, também foram cobertos os edge cases em que alguma exceção era lançada.  
# Issues

MapStruct

Encontrei alguns problemas para fazer funcionar o mapstruct junto com o lombok no Eclipse.

Inicialmente o mapstruct precisa da habilitacao do Annotation Processing em: No projeto Properties>Java Compiler> Annotation Processing> Enable project specific setting.

Feito isso eh necessario adicionar ao pom.xml o lombok-mapstruct-binding conforme descrito em: https://stackoverflow.com/questions/47676369/mapstruct-and-lombok-not-working-together

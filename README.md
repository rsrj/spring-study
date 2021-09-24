# spring-study
Repositorio para salvar toda a etapa de aprendizado do spring

#Issues
MapStruct
Encontrei alguns problemas para fazer funcionar o mapstruct junto com o lombok no Eclipse.

Inicialmente o mapstruct precisa da habilitacao do Annotation Processing em: No projeto Properties>Java Compiler> Annotation Processing> Enable project specific setting.
Feito isso eh necessario adicionar ao pom.xml o lombok-mapstruct-binding conforme descrito em: https://stackoverflow.com/questions/47676369/mapstruct-and-lombok-not-working-together

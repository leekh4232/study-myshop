@startuml
skinparam dpi 200

package kr.hossam.myshop.controllers {
    
    class ClassName1 {
        +ClassName1(param1: Type1)
        +method1(): ReturnType1
        +method2(param2: Type2): ReturnType2
    }

    class ClassName2 {
        +ClassName2(param1: Type1)
        +method1(): ReturnType1
    }

    class ClassName3 {
        +ClassName3(param1: Type1)
        +method1(param2: Type2): ReturnType2
    }

    ClassName1 --|> ClassName2 : IsA
    ClassName1 --> ClassName3 : HasA

    // Add more classes and relationships as needed
}

@enduml
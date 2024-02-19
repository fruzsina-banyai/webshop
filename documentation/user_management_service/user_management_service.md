# User Management Service

## Entity relations

```mermaid
erDiagram
    USER ||--o{ ADDRESS : has
```

## Package relations

```mermaid
flowchart LR
    A[MODEL] --> B[REPOSITORY]
    C[SERVICE] --> B
    D[CONTROLLER] --> C
```

### Model package

```mermaid
classDiagram
    User "1" *-- "0..*" Address
    class User {
        -userId : UUID
        -role: String
        -firstName : String
        -lastName : String
        -email : String
        -phoneNumber : String
        -password : String
    }
    class Address {
        -addressId : UUID
        -userId: UUID
        -country : String
        -state : String
        -zipCode : String
        -city : String
        -streetAdress : String     
    }

```

### Repository package

```mermaid
classDiagram
    class UserRepository {      
    }
    class AddressRepository {
        +findByUserId(userId) List "Address"       
    }
```

### Service package
```mermaid
classDiagram
    class UserService {      
        -userRepository : UserRepository
        -addressRepository : AddressRepository
        -passwordEncoder: passwordEncoder
        +createUser(user: User) User
        +deleteUser(userId: UUID)
        +updateUser(user: User) User
        +changePassword(userId: UUID, password: String) User
        +findUserById(userId: UUID) User
        +getAddresses() List "Address"
        +findAllUsers() List "User"
    }
    class AddressService { 
        -addressRepository : AddressRepository
        +createAddress(address: Address) Address
        +deleteAddress(id: UUID)
        +updateAddress(address: Address) Address
        +findAddressById(id: UUID) Address
        +findAllAddresses() List "Address"
    }
```

### Controller package
```mermaid
classDiagram
    class UserController {      
        -userService : UserService
        +createUser(userDto: UserDto) ResponseEntity "UserDto"
        +deleteUser(userId: UUID) ResponseEntity "Unit"
        +updateUser(userDto: UserDto) ResponseEntity "UserDto"
        +changePassword(userId: UUID, passwordChangeDto: PasswordChangeDto) ResponseEntity "UserDto"
        +findUserById(userId: UUID) ResponseEntity "UserDto"
        +getAddresses() ResponseEntity "List "Address""
        +findAllUsers() ResponseEntity "List "User""
    }
    class AddressController { 
        -addressService : AddressService
        +createAddress(addressDto: AddressDto) ResponseEntity "AddressDto"
        +deleteAddress(addressId: UUID) ResponseEntity "Unit"
        +updateAddress(addressDto: AddressDto) ResponseEntity "AddressDto"
        +findAddressById(addressId: UUID) ResponseEntity "AddressDto"
        +findAllAddresses() ResponseEntity "List "AddressDto""
    }
```

As a new user, I want to be able to create a new account securely, with my email address, password and other personal data, so that I can access personalized features in the web shop.

---

As a registered user, I want to be able to verify my email address, so that no one else can use it, except me.

As a registered user, I want to be able to log in to the web shop securely, with my email address and password, so that access my profile.

As a registered user, I want to be able to reset my password through a secure process using my email address, so that I can regain access to my account.

As a registered user, I want to be able to delete my account from the web shop, so that my personal data is no longer stored on the platform.

As a registered user, I want to receive email notifications for important account-related activities, so that I can stay informed about my account and filter out suspicious activities.

---

As a logged in user, I want to be able to change any personal data I registered with, so that I can always keep my user info up to date.

As a logged in user, I want to be able to manage my addresses, so that I can make sure they are correct.

---

As an admin user, I want to be able to view user accounts, so that I can monitor user activity.

As an admin user, I want to be able to reset passwords for user accounts, so that I can assist users.

As an admin user, I want to have access to user contact information, so that I can assist users effectively.

As an admin user, I want to be able to create and manage user groups with roles and permissions, so that I can organize users.

As an admin user, I want to be able to view a log of user-related activities, including account creations, password changes, and role modifications, so that I can track changes.

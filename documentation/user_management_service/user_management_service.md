# User Management Service

## Entity relations

```mermaid
erDiagram
    USER ||--o{ ADDRESS : has
    USER }o--|{ USER_GROUP : "is assigned to"
    USER_GROUP }o--o{ USER_ROLE : "is assigned to"
    USER_ROLE }o--|{ PERMISSION : has
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
    User "1..*" --o "0..*" UserGroup
    UserGroup "0..*" o-- "0..*" UserRole
    UserRole "0..*" o-- "0..*" Permission
    class User {
        -userId : String
        -firstName : String
        -lastName : String
        -email : String
        -phoneNumber : String
        -password : String
    }
    class Address {
        -addressId : String
        -country : String
        -state : String
        -zipCode : String
        -city : String
        -streetAdress : String
    }
    class UserGroup {
        -groupId : String
        -groupName : String
        -groupDescription : String
    }
    class UserRole {
        -roleId : String
        -roleName : String
        -roleDescription : String
    }
    class Permission {
        -permissionId : String
        -permissionName : String
        -permissionDescription : String
    }

```

### Repository package

```mermaid
classDiagram
    class PermissionRepository {        
    }
    class UserRoleRepository {        
    }
    class UserGroupRepository {        
    }
    class UserRepository {      
    }
    class AddressRepository {        
    }
```

### Service package
```mermaid
classDiagram
    class PermissionService {        
        -permissionRepository : PermissionRepository
        +createPermission() Permisson
        +deletePermission() 
        +updatePermission() Permission
        +findPermissionById() Permission
    }
    class UserRoleService {     
        -userRoleRepository : UserRoleRepository  
        -permissionRepository : PermissionRepository
        +createRole() UserRole
        +deleteRole() 
        +updateRole() UserRole
        +findRoleById() UserRole
        +getPermissions() List<'Permission'>
        +addPermissions() List<'Permission'>
        +removePermissions()
    }
    class UserGroupService {      
        -userGroupRepository : UserGroupRepository  
        -userRoleRepository : UserRoleRepository
        -userRepository : UserRepository
        +createGroup() UserGroup
        +deleteGroup()
        +updateGroup() UserGroup
        +findGroupById() UserGroup
        +getMembers() List<'User'>
        +addMembers() List<'User'>
        +removeMembers()
        +getRoles() List<'UserRole'>
        +addRoles() List<'UserRole'>
        +removeRoles()
    }
    class UserService {      
        -userRepository : UserRepository
        -addressRepository : AddressRepository
        +createUser() User
        +deleteUser()
        +updateUser() User
        +findUserById() User
        +getAddresses() List<'Address'>
        +addAddresses() List<'Address'>
        +removeAddresses()
    }
    class AddressService { 
        -addressRepository : AddressRepository
        +createAddress() Address
        +deleteAddress()
        +updateAddress() Address
        +findAddressById() Address
    }
```

### Controller package
```mermaid
classDiagram
    class PermissionController {        
        -permissionService : PermissionService
        +createPermission() ResponseEntity
        +deletePermission() ResponseEntity
        +updatePermission() ResponseEntity
        +findPermissionById() ResponseEntity
    }
    class UserRoleController {     
        -userRoleService : UserRoleService
        +createRole() ResponseEntity
        +deleteRole() ResponseEntity
        +updateRole() ResponseEntity
        +findRoleById() ResponseEntity
        +getPermissions() ResponseEntity
        +addPermissions() ResponseEntity
        +removePermissions() ResponseEntity
    }
    class UserGroupController {      
        -userGroupService : UserGroupService
        +createGroup() ResponseEntity
        +deleteGroup() ResponseEntity
        +updateGroup() ResponseEntity
        +findGroupById() ResponseEntity
        +getMembers() ResponseEntity
        +addMembers() ResponseEntity
        +removeMembers() ResponseEntity
        +getRoles() ResponseEntity
        +addRoles() ResponseEntity
        +removeRoles() ResponseEntity
    }
    class UserController {      
        -userService : UserService
        +createUser() ResponseEntity
        +deleteUser() ResponseEntity
        +updateUser() ResponseEntity
        +findUserById() ResponseEntity
        +getAddresses() ResponseEntity
        +addAddresses() ResponseEntity
        +removeAddresses() ResponseEntity
    }
    class AddressController { 
        -addressService : AddressService
        +createAddress() ResponseEntity
        +deleteAddress() ResponseEntity
        +updateAddress() ResponseEntity
        +findAddressById() ResponseEntity
    }
```

As a new user, I want to be able to create a new account securely, with my email address, password and other personal data, so that I can access personalized features in the web shop.

---

As a registered user, I want to be able to verify my email address, so that no one else can use it, except me.

As a registered user, I want to be able to log in to the web shop securely, with my email address and password, so that access my profile.

As a registered user, I want to be able to reset my password through a secure process using my email address, so that I can regain access to my account.

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


As a new user, I want to be able to create a new account securely, with my email address, password and other personal data, so that I can access personalized features in the web shop.

---

As a registered user, I want to be able to verify my email address, so that no one else can use it, except me.

As a registered user, I want to be able to log in to the web shop securely, with my email address and password, so that access my profile.

As a registered user, I want to be able to reset my password through a secure process using my email address, so that I can regain access to my account.

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


# 🎯아웃소싱 프로젝트, Task flow

------

## 🗨️프로젝트 소개
이 프로젝트는 업무 등 협업 관계에서 일의 진행도 혹은 통계를 보기 위한 **태스크 플로우 웹 애플리케이션**입니다.  
로그인 한 유저만 사용할 수 있습니다.  
사용자는 작업 일정을 생성하거나 진행도를 변경할 수 있으며, 모든 작업에는 댓글을 달 수 있습니다.  
통합 검색, 통계(대시보드) 보기를 제공합니다.

---

## ✨주요 기능

- **회원가입** : 새로운 사용자가 회원가입을 진행할 수 있음.
- **JWT 인증 사용** : 로그인/로그아웃 및 로그인 필터 기능 구현.
- **내 로그인 정보 보기** : 로그인 하고 있는 계정의 정보 확인 가능.
- **Task CRUD 및 상태 수정** : Task의 작성, 내용 수정, 삭제, 조회 및 상태 수정이 가능함.
- **Team CRUD** : Team의 생성, 수정, 삭제, 조회 및 추가 가능한 사용자의 조회 가능함.
- **Comment CRUD** : 댓글/대댓글의 생성, 수정, 삭제, 조회 가능함.
- **Dashboard** : 대시보드 통계, 내 작업 요약, 팀 진행률 및 최근 활동을 조회 가능함.
- **activity** : 활동 로그 조회 가능함.
- **search** : 쿼리를 기준으로 통합 및 작업 검색이 가능함.

---

## 🪄기술 스택
<img src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white">
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white">
<img src="https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens">
<img src="https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white">
<img src="https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white">

---

## 📖ERD

https://www.erdcloud.com/d/JCo3Jh32iPy9w5Xxf

---

## 📄API 명세

### 공통 응답
```Http
HTTP/1.1 {code}
Content-Type: application/json;
```
```JSON
{
    "success": true,           
    "message": "성공 메시지",   
    "data": {},               
    "timestamp": "2024-03-21T10:00:00Z"
}
```

| Parameter | Type            | Description                      |
| :-------- |:----------------| :-------------------------       |
| success   | `boolean`       | **Required**. 요청 성공 여부      |
| message   | `String`        | **Required**. 성공/실패 메시지    |
| data      | `Object`        | Response Data (Null 가능)        |
| timestamp | `LocalDateTime` | **Required**. ISO 8601 형식      |

### User
| 기능           | method  | Domain | EndPoint |
|:-------------| :---    | :----  | :----  |
| 현재 사용자 정보 조회 | `GET`| `user`| /api/users/me |

<details>
<summary> 현재 사용자 정보 조회 </summary>

#### Request
```Http
GET /api/users/me
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
```JSON
{

}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": "true",
  "message": "프로필 조회 성공",
  "data": {
    "name" : "이름",
    "email" : "user@email.com",
    "role" : "USER"
  },
  "timestamp": "2025-09-02"
}
```
</details>

---

### Auth
| 기능    | method  | Domain | EndPoint |
|:------| :---    | :----  | :----  |
| 로그인   | `POST`| `auth`| /api/auth/login |
| 회원 가입 | `POST`| `auth`| /api/auth/register|
| 로그아웃  | `POST`| `auth`| /api/auth/logout|
| 계정 삭제 | `POST`| `auth`| /api/auth/withdraw|

<details>
<summary> 로그인 </summary>

#### Request
```Http
POST /api/auth/login
```
```JSON
{
  "nickName": "test1",
  "password": "a12345!A"
}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "로그인이 완료되었습니다.",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huZG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJleHAiOjE1MTYzMjU0MjJ9"
  },
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>

<details>
<summary> 회원가입 </summary>

#### Request
```Http
POST /api/auth/register
```
```JSON
{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "Password123!",
  "name": "John Doe"
}
```

#### Response
```Http
HTTP/1.1 201
Content-Type: application/json;
```
```JSON

{
  "success": true,
  "message": "회원가입이 완료되었습니다.",
  "data": {
    "id": 1,
    "username": "johndoe",
    "email": "john@example.com",
    "name": "John Doe",
    "role": "USER",
    "createdAt": "2024-03-21T10:00:00Z"
  },
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>

<details>
<summary> 로그아웃 </summary>

#### Request
```Http
POST /api/auth/logout
```
```JSON
{

}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "로그 아웃이 완료되었습니다.",
  "data": null,
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>
<details>
<summary> 계정 삭제 </summary>

#### Request
```Http
POST /api/auth/withdraw
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
```JSON
{
  "password": "current_password"
}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "회원탈퇴가 완료되었습니다.",
  "data": null,
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>

---

### Task
| 기능          | method  | Domain | EndPoint |
|:------------| :---    | :----  | :----  |
| task 생성     | `POST`| `task`|/api/tasks|
| task 목록 조회  | `GET`| `task`|/api/tasks|
| task 상세 조회  | `GET`| `task`|/tasks/{taskId}/comments?page=0&size=10|
| task 수정     | `PUT`| `task`|/tasks/{taskId}|
| task 상태 변경  | `PATCH`| `task`|/tasks/{taskId}/status|
| task 삭제     | `DELETE`| `task`|/tasks/{taskId}|
| task 담당자 조회 | `GET`| `task`|/tasks/{taskId}/assignee|

<details>
<summary> task 생성 </summary>

#### Request
```Http
POST /tasks
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
```JSON
{
  "title": "프로젝트 기획안 작성",
  "description": "2024년 1분기 프로젝트 기획안 작성하기",
  "dueDate": "2024-04-01T23:59:59Z",
  "priority": "HIGH",
  "assigneeId": 1
}
```

#### Response
```Http
HTTP/1.1 201
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "Task가 생성되었습니다.",
  "data": {
    "id": 1,
    "title": "프로젝트 기획안 작성",
    "description": "2024년 1분기 프로젝트 기획안 작성하기",
    "dueDate": "2024-04-01T23:59:59Z",
    "priority": "HIGH",
    "status": "TODO",
    "assigneeId": 1,
    "assignee": {
      "id": 1,
      "username": "johndoe",
      "name": "John Doe",
      "email": "john@example.com"
    },
    "createdAt": "2024-03-21T10:00:00Z",
    "updatedAt": "2024-03-21T10:00:00Z"
  },
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>
<details>
<summary> task 목록 조회 </summary>

#### Request
```Http
GET /api/tasks?status=TODO&page=0&size=10&search=기획&assigneeId=1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
```JSON
{

}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "Task 목록을 조회했습니다.",
  "data": {
    "content": [
      {
        "id": 1,
        "title": "프로젝트 기획안 작성",
        "description": "2024년 1분기 프로젝트 기획안 작성하기",
        "dueDate": "2024-04-01T23:59:59Z",
        "priority": "HIGH",
        "status": "TODO",
        "assigneeId": 1,
        "assignee": {
          "id": 1,
          "username": "johndoe",
          "name": "John Doe",
          "email": "john@example.com"
        },
        "createdAt": "2024-03-21T10:00:00Z",
        "updatedAt": "2024-03-21T10:00:00Z"
      }
    ],
    "totalElements": 1,
    "totalPages": 1,
    "size": 10,
    "number": 0
  },
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>
<details>
<summary> task 상세 조회</summary>

#### Request
```Http
GET /tasks/{taskId}/comments?page=0&size=10
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
```JSON
{

}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "Task를 조회했습니다.",
  "data": {
    "id": 1,
    "title": "프로젝트 기획안 작성",
    "description": "2024년 1분기 프로젝트 기획안 작성하기",
    "dueDate": "2024-04-01T23:59:59Z",
    "priority": "HIGH",
    "status": "TODO",
    "assigneeId": 1,
    "assignee": {
      "id": 1,
      "username": "johndoe",
      "name": "John Doe",
      "email": "john@example.com"
    },
    "createdAt": "2024-03-21T10:00:00Z",
    "updatedAt": "2024-03-21T10:00:00Z"
  },
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>
<details>
<summary> task 수정</summary>

#### Request
```Http
PUT /api/tasks/{taskId}
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
```JSON
{
  "title": "프로젝트 기획안 수정",
  "description": "2024년 1분기 프로젝트 기획안 수정하기",
  "dueDate": "2024-04-02T23:59:59Z",
  "priority": "MEDIUM",
  "status": "IN_PROGRESS",
  "assigneeId": 2
}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "Task가 수정되었습니다.",
  "data": {
    "id": 1,
    "title": "프로젝트 기획안 수정",
    "description": "2024년 1분기 프로젝트 기획안 수정하기",
    "dueDate": "2024-04-02T23:59:59Z",
    "priority": "MEDIUM",
    "status": "IN_PROGRESS",
    "assigneeId": 2,
    "assignee": {
      "id": 2,
      "username": "janedoe",
      "name": "Jane Doe",
      "email": "jane@example.com"
    },
    "createdAt": "2024-03-21T10:00:00Z",
    "updatedAt": "2024-03-21T10:30:00Z"
  },
  "timestamp": "2024-03-21T10:30:00Z"
}
```
</details>
<details>
<summary> task 상태 변경</summary>

#### Request
```Http
PATCH /api/tasks/{taskId}/status
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
```JSON
{
  "status": "IN_PROGRESS"
}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "작업 상태가 업데이트되었습니다.",
  "data": {
    "id": 3,
    "title": "API 문서 작성",
    "description": "RESTful API 문서 작성하기",
    "status": "IN_PROGRESS",
    "priority": "MEDIUM",
    "assigneeId": 1,
    "assignee": {
      "id": 1,
      "username": "janedoe",
      "name": "Jane Doe",
      "email": "jane@example.com"
    },
    "createdAt": "2024-03-21T14:00:00Z",
    "updatedAt": "2024-03-21T14:30:00Z",
    "dueDate": "2024-03-28T14:30:00Z"
  },
  "timestamp": "2024-03-21T14:30:00Z"
}
```
</details>
<details>
<summary> task 삭제</summary>

#### Request
```Http
DELETE /api/tasks/{taskId}
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
```JSON
{

}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "Task가 삭제되었습니다.",
  "data": null,
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>
<details>
<summary>task 담당자 조회(추가적으로 필요한 API)</summary>

#### Request
```Http
GET /tasks/{taskId}/assignee
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
```JSON
{

}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "태스크 담당자 조회 성공",
  "data": {
    "assignee": {
      "id": 5,
      "name": "홍길동"
    }
  },
  "timestamp": "2025-09-02T14:55:00.123Z"
}
```
</details>

---

### Team
| 기능               | method  | Domain | EndPoint |
|:-----------------| :---    | :----  | :----  |
| team 생성          | `POST`| `team`|/api/teams|
| team 목록 조회       | `GET`| `team`|/api/teams|
| team 정보 수정       | `PUT`| `team`|/api/teams/{teamId}|
| team 삭제          | `DELETE`| `team`|/api/teams/{teamId}|
| team member 추가   | `POST`| `team`|/api/teams/{teamId}/members|
| team member 삭제   | `POST`| `team`|/api/teams/{teamId}/members/{userId}|
| 추가 가능한 사용자 목록 조회 | `GET`| `team`|/api/users/available?teamId={teamId}|
| 특정 팀 조회      | `GET`| `team`|/api/teams/{teamId}|
| 팀 멤버 목록 조회       | `GET`| `team`|/api/teams/{teamId}/members|

<details>
<summary>team 생성</summary>

#### Request
```Http
POST /api/teams
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
```JSON
{
  "name": "팀 이름",
  "description": "팀 설명"
}
```

#### Response
```Http
HTTP/1.1 201
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "팀이 성공적으로 생성되었습니다.",
  "data": {
    "id": 3,
    "name": "디자인팀",
    "description": "UI/UX 디자이너들",
    "createdAt": "2024-03-21T10:00:00Z",
    "members": []
  },
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>
<details>
<summary>team 목록 조회</summary>

#### Request
```Http
GET /api/teams
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
```JSON
{

}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "팀 목록을 조회했습니다.",
  "data": [
    {
      "id": 1,
      "name": "개발팀",
      "description": "프론트엔드 및 백엔드 개발자들",
      "createdAt": "2024-03-21T10:00:00Z",
      "members": [
        {
          "id": 1,
          "username": "admin",
          "name": "관리자",
          "email": "admin@example.com",
          "role": "ADMIN",
          "createdAt": "2024-03-21T09:00:00Z"
        },
        {
          "id": 2,
          "username": "johndoe",
          "name": "김철수",
          "email": "john@example.com",
          "role": "USER",
          "createdAt": "2024-03-21T09:00:00Z"
        }
      ]
    }
  ],
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>
<details>
<summary>team 정보 수정</summary>

#### Request
```Http
PUT /api/teams/{teamId}
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
```JSON
{
  "name": "프론트엔드팀",
  "description": "프론트엔드 전문 개발팀"
}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "팀 정보가 성공적으로 업데이트되었습니다.",
  "data": {
    "id": 1,
    "name": "프론트엔드팀",
    "description": "프론트엔드 전문 개발팀",
    "createdAt": "2024-03-21T10:00:00Z",
    "members": [
      {
        "id": 1,
        "username": "admin",
        "name": "관리자",
        "email": "admin@example.com",
        "role": "ADMIN",
        "createdAt": "2024-03-21T09:00:00Z"
      }
    ]
  },
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>
<details>
<summary>team 삭제</summary>

#### Request
```Http
DELETE /api/teams/{teamId}
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
```JSON
{

}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "팀이 성공적으로 삭제되었습니다.",
  "data": {
    "message": "팀이 성공적으로 삭제되었습니다"
  },
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>
<details>
<summary>team member 추가</summary>

#### Request
```Http
POST /api/teams/{teamId}/members
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
```JSON
{
  "userId": 3
}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "멤버가 성공적으로 추가되었습니다.",
  "data": {
    "id": 1,
    "name": "개발팀",
    "description": "프론트엔드 및 백엔드 개발자들",
    "createdAt": "2024-03-21T10:00:00Z",
    "members": [
      {
        "id": 1,
        "username": "admin",
        "name": "관리자",
        "email": "admin@example.com",
        "role": "ADMIN",
        "createdAt": "2024-03-21T09:00:00Z"
      },
      {
        "id": 3,
        "username": "janedoe",
        "name": "이영희",
        "email": "jane@example.com",
        "role": "USER",
        "createdAt": "2024-03-21T09:00:00Z"
      }
    ]
  },
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>
<details>
<summary>team member 삭제</summary>

#### Request
```Http
DELETE /api/teams/{teamId}/members/{userId}
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
```JSON
{

}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "멤버가 성공적으로 제거되었습니다.",
  "data": {
    "id": 1,
    "name": "개발팀",
    "description": "프론트엔드 및 백엔드 개발자들",
    "createdAt": "2024-03-21T10:00:00Z",
    "members": [
      {
        "id": 1,
        "username": "admin",
        "name": "관리자",
        "email": "admin@example.com",
        "role": "ADMIN",
        "createdAt": "2024-03-21T09:00:00Z"
      }
    ]
  },
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>
<details>
<summary>추가 가능한 사용자 목록 조회</summary>

#### Request
```Http
GET /api/users/available?teamId={teamId}
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
```JSON
{

}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "사용 가능한 사용자 목록을 조회했습니다.",
  "data": [
    {
      "id": 3,
      "username": "janedoe",
      "name": "이영희",
      "email": "jane@example.com",
      "role": "USER",
      "createdAt": "2024-03-21T09:00:00Z"
    },
    {
      "id": 4,
      "username": "newuser",
      "name": "박민수",
      "email": "newuser@example.com",
      "role": "USER",
      "createdAt": "2024-03-21T09:00:00Z"
    }
  ],
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>
<details>
<summary>특정 팀 조회</summary>

#### Request
```Http
GET /api/teams/{teamId}
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
```JSON
{

}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "팀 정보를 조회했습니다.",
  "data": {
    "id": 1,
    "name": "개발팀",
    "description": "프론트엔드 및 백엔드 개발자들",
    "createdAt": "2024-03-21T10:00:00Z",
    "members": [
      {
        "id": 1,
        "username": "admin",
        "name": "관리자",
        "email": "admin@example.com",
        "role": "ADMIN",
        "createdAt": "2024-03-21T09:00:00Z"
      }
    ]
  },
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>
<details>
<summary>팀 멤버 목록 조회</summary>

#### Request
```Http
GET /api/teams/{teamId}/members
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
```JSON
{

}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "팀 멤버 목록을 조회했습니다.",
  "data": [
    {
      "id": 1,
      "username": "admin",
      "name": "관리자",
      "email": "admin@example.com",
      "role": "ADMIN",
      "createdAt": "2024-03-21T09:00:00Z"
    },
    {
      "id": 2,
      "username": "johndoe",
      "name": "김철수",
      "email": "john@example.com",
      "role": "USER",
      "createdAt": "2024-03-21T09:00:00Z"
    }
  ],
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>

---

### Comment
| 기능  | method  | Domain | EndPoint |
| :--   | :---    | :----  | :----  |
|comment 생성| `POST`| `comment`|/api/tasks/{taskId}/comments|
|comment 조회| `GET`| `comment`|/api/tasks/{taskId}/comments?page=0&size=10&sort=newest|
|comment 수정| `PUT`| `comment`|/api/tasks/{taskId}/comments/{commentId}|
|comment 삭제| `DELETE`| `comment`|/api/tasks/{taskId}/comments/{commentId}|

<details>
<summary>comment 생성</summary>

#### Request
```Http
POST /tasks/{taskId}/comments
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
```JSON
{
  "content": "기획안 초안 검토 완료했습니다.",
  "parentId": 5
}
```

#### Response
```Http
HTTP/1.1 201
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "댓글이 생성되었습니다.",
  "data": {
    "id": 1,
    "content": "기획안 초안 검토 완료했습니다.",
    "taskId": 1,
    "userId": 1,
    "user": {
      "id": 1,
      "username": "johndoe",
      "name": "John Doe",
      "email": "john@example.com",
      "role": "USER"
    },
    "parentId": 5,
    "createdAt": "2024-03-21T10:00:00Z",
    "updatedAt": "2024-03-21T10:00:00Z"
  },
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>
<details>
<summary>comment 조회</summary>

#### Request
```Http
GET /api/tasks/{taskId}/comments?page=0&size=10&sort=newest
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
```JSON
{

}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "댓글 목록을 조회했습니다.",
  "data": {
    "content": [
      {
        "id": 1,
        "content": "기획안 초안 검토 완료했습니다.",
        "taskId": 1,
        "userId": 1,
        "user": {
          "id": 1,
          "username": "johndoe",
          "name": "John Doe",
          "email": "john@example.com",
          "role": "USER"
        },
        "createdAt": "2024-03-21T10:00:00Z",
        "updatedAt": "2024-03-21T10:00:00Z"
      },
      {
        "id": 2,
        "content": "감사합니다. 수정사항을 반영하겠습니다.",
        "taskId": 1,
        "userId": 2,
        "user": {
          "id": 2,
          "username": "janedoe",
          "name": "Jane Doe",
          "email": "jane@example.com",
          "role": "USER"
        },
        "parentId": 1,
        "createdAt": "2024-03-21T10:15:00Z",
        "updatedAt": "2024-03-21T10:15:00Z"
      }
    ],
    "totalElements": 15,
    "totalPages": 2,
    "size": 10,
    "number": 0
  },
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>
<details>
<summary>comment 수정</summary>

#### Request
```Http
PUT /api/tasks/{taskId}/comments/{commentId}
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
```JSON
{
  "content": "기획안 초안 검토 완료했습니다. 수정사항 반영 필요."
}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "댓글이 수정되었습니다.",
  "data": {
    "id": 1,
    "content": "기획안 초안 검토 완료했습니다. 수정사항 반영 필요.",
    "taskId": 1,
    "userId": 1,
    "user": {
      "id": 1,
      "username": "johndoe",
      "name": "John Doe",
      "email": "john@example.com",
      "role": "USER"
    },
    "parentId": null,
    "createdAt": "2024-03-21T10:00:00Z",
    "updatedAt": "2024-03-21T10:30:00Z"
  },
  "timestamp": "2024-03-21T10:30:00Z"
}
```
</details>
<details>
<summary>comment 삭제</summary>

#### Request
```Http
DELETE /api/tasks/{taskId}/comments/{commentId}
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
```JSON
{

}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "댓글이 삭제되었습니다.",
  "data": null,
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>

---

### Dashboard
| 기능  | method  | Domain | EndPoint |
| :--   | :---    | :----  | :----  |
|대시보드 통계 조회| `GET`| `dashboard`|/api/dashboard/stats|
|내 작업 요약 조회| `GET`| `dashboard`|/api/dashboard/my-tasks|
|팀 진행률 조회| `GET`| `dashboard`|/api/dashboard/team-progress|
|최근 활동 조회| `GET`| `dashboard`|/api/dashboard/activities?page=0&size=10|

<details>
<summary>대시보드 통계 조회</summary>

#### Request
```Http
GET /api/dashboard/stats
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
```JSON
{

}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "대시보드 통계 조회 완료",
  "data": {
    "totalTasks": 15,
    "completedTasks": 8,
    "inProgressTasks": 4,
    "todoTasks": 3,
    "overdueTasks": 2,
    "teamProgress": 65,
    "myTasksToday": 5,
    "completionRate": 53
  },
  "timestamp": "2024-01-01T10:00:00.000Z"
}
```
</details>
<details>
<summary>내 작업 요약 조회</summary>

#### Request
```Http
GET /api/dashboard/my-tasks
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
```JSON
{

}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
"success": true,
"message": "내 작업 요약 조회 완료",
"data": {
"todayTasks": [
{
"id": 1,
"title": "버그 수정",
"status": "IN_PROGRESS",
"dueDate": "2024-01-01T23:59:59.000Z"
}
],
"upcomingTasks": [
{
"id": 2,
"title": "새 기능 개발",
"status": "TODO",
"dueDate": "2024-01-05T23:59:59.000Z"
}
],
"overdueTasks": [
{
"id": 3,
"title": "문서 작성",
"status": "TODO",
"dueDate": "2023-12-30T23:59:59.000Z"
}
]
},
"timestamp": "2024-01-01T10:00:00.000Z"
}
```
</details>
<details>
<summary>팀 진행률 조회</summary>

#### Request
```Http
GET /api/dashboard/team-progress
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
```JSON
{

}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "팀 진행률 조회 완료",
  "data": {
    "개발팀": 75,
    "디자인팀": 60,
    "QA팀": 85
  },
  "timestamp": "2024-01-01T10:00:00.000Z"
}
```
</details>
<details>
<summary>최근 활동 조회</summary>

#### Request
```Http
GET /api/dashboard/activities?page=0&size=10
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
```JSON
{

}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "활동 내역 조회 완료",
  "data": {
    "content": [
      {
        "id": 1,
        "userId": 2,
        "user": {
          "id": 2,
          "name": "김철수"
        },
        "action": "created_task",
        "targetType": "task",
        "targetId": 5,
        "description": "새 작업을 생성했습니다",
        "createdAt": "2024-01-01T09:30:00.000Z"
      }
    ],
    "totalElements": 25,
    "totalPages": 3,
    "size": 10,
    "number": 0
  },
  "timestamp": "2024-01-01T10:00:00.000Z"
}
```
</details>

---

### activity
| 기능  | method  | Domain | EndPoint |
| :--   | :---    | :----  | :----  |
|활동 로그 조회| `GET`| `activity`|/api/activities|

<details>
<summary>활동 로그 조회</summary>

#### Request
```Http
GET /api/activities
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
```JSON
{

}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "활동 로그를 조회했습니다.",
  "data": {
    "content": [
      {
        "id": 1,
        "type": "TASK_CREATED",
        "userId": 1,
        "user": {
          "id": 1,
          "username": "admin",
          "name": "관리자",
          "email": "admin@example.com",
          "role": "ADMIN",
          "createdAt": "2024-03-21T09:00:00Z"
        },
        "taskId": 5,
        "timestamp": "2024-03-21T10:30:00Z",
        "description": "새로운 작업 '로그인 버그 수정'을 생성했습니다."
      },
      {
        "id": 2,
        "type": "TASK_STATUS_CHANGED",
        "userId": 2,
        "user": {
          "id": 2,
          "username": "johndoe",
          "name": "김철수",
          "email": "john@example.com",
          "role": "USER",
          "createdAt": "2024-03-21T09:00:00Z"
        },
        "taskId": 3,
        "timestamp": "2024-03-21T10:15:00Z",
        "description": "작업 상태를 TODO에서 IN_PROGRESS로 변경했습니다."
      },
      {
        "id": 3,
        "type": "COMMENT_CREATED",
        "userId": 3,
        "user": {
          "id": 3,
          "username": "janedoe",
          "name": "이영희",
          "email": "jane@example.com",
          "role": "USER",
          "createdAt": "2024-03-21T09:00:00Z"
        },
        "taskId": 2,
        "timestamp": "2024-03-21T09:45:00Z",
        "description": "작업에 댓글을 작성했습니다."
      },
      {
        "id": 4,
        "type": "TASK_UPDATED",
        "userId": 1,
        "user": {
          "id": 1,
          "username": "admin",
          "name": "관리자",
          "email": "admin@example.com",
          "role": "ADMIN",
          "createdAt": "2024-03-21T09:00:00Z"
        },
        "taskId": 1,
        "timestamp": "2024-03-21T09:30:00Z",
        "description": "작업 정보를 수정했습니다."
      },
      {
        "id": 5,
        "type": "TASK_DELETED",
        "userId": 1,
        "user": {
          "id": 1,
          "username": "admin",
          "name": "관리자",
          "email": "admin@example.com",
          "role": "ADMIN",
          "createdAt": "2024-03-21T09:00:00Z"
        },
        "taskId": 7,
        "timestamp": "2024-03-21T09:00:00Z",
        "description": "작업을 삭제했습니다."
      }
    ],
    "totalElements": 25,
    "totalPages": 5,
    "size": 10,
    "number": 0
  },
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>

---

### search
| 기능  | method  | Domain | EndPoint |
| :--   | :---    | :----  | :----  |
|통합 검색| `GET`|`search`|/api/search?q={query}|
|작업 검색| `GET`|`search`|/api/tasks/search?q={query}&page=0&size=10|

<details>
<summary>통합 검색</summary>

#### Request
```Http
GET /api/search?q={query}
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
```JSON
{

}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "검색 완료",
  "data": {
    "tasks": [
      {
        "id": 1,
        "title": "사용자 인증 구현",
        "description": "JWT 인증 시스템 구현",
        "status": "DONE",
        "assignee": {
          "id": 1,
          "name": "관리자"
        }
      }
    ],
    "users": [
      {
        "id": 2,
        "username": "johndoe",
        "name": "김철수",
        "email": "john@example.com"
      }
    ],
    "teams": [
      {
        "id": 1,
        "name": "개발팀",
        "description": "프론트엔드 및 백엔드 개발자들"
      }
    ]
  },
  "timestamp": "2024-01-01T10:00:00.000Z"
}
```
</details>
<details>
<summary>작업 검색</summary>

#### Request
```Http
GET /api/tasks/search?q={query}&page=0&size=10
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
```JSON
{

}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "작업 검색 완료",
  "data": {
    "content": [
      {
        "id": 1,
        "title": "사용자 인증 구현",
        "description": "JWT 인증 시스템 구현",
        "status": "DONE",
        "priority": "HIGH",
        "assigneeId": 1,
        "assignee": {
          "id": 1,
          "name": "관리자"
        },
        "createdAt": "2024-01-01T09:00:00.000Z",
        "updatedAt": "2024-01-01T09:30:00.000Z",
        "dueDate": "2024-01-05T23:59:59.000Z"
      }
    ],
    "totalElements": 5,
    "totalPages": 1,
    "size": 10,
    "number": 0
  },
  "timestamp": "2024-01-01T10:00:00.000Z"
}
```
</details>

---

## 테스트 커버리지
<img width="427" height="131" alt="Image" src="https://github.com/user-attachments/assets/a4730a8a-8d8a-4bca-b666-48f795092db9" />

## 👥기여자

- [@Lunarltn](https://www.github.com/Lunarltn)
- [@lby9906](https://www.github.com/lby9906)
- [@rlawprud](https://www.github.com/rlawprud)
- [@eunjin0468](https://www.github.com/eunjin0468)
- [@doldollee00](https://www.github.com/doldollee00)

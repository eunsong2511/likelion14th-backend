package com.example.demo.DTO;
//값 전달 시 이용하는 객체
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class MemberDTO {

    private MemberDTO(){
    }

    //요청받았을때 객체
    public static class Request {
        @Data //@setter+@getter+... 등
        @NoArgsConstructor
        public static class Create {
            private String userId;
            private String password;
            private String username;
        }

        @Data
        @NoArgsConstructor
        public static class Update {
            private String username;
            private String password;
        }

        @Data
        @NoArgsConstructor
        public static class Login {
            private String userId;
            private String password;
        }
    }

    public static class Response {
        @Data
        @AllArgsConstructor
        public static class Member{
            private Long id;
            private String userId;
            private String username;
        }
    }

    @Data
    @AllArgsConstructor
    public static class Result<T>{ //응답데이터 감싸는 공통포맷 제네릭이용(타입일반화, 여러 타입을 쓸수있도록)
        private T data;

    }
}

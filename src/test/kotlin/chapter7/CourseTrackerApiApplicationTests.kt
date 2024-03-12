// package chapter7
//
// import com.fasterxml.jackson.databind.ObjectMapper
// import com.jayway.jsonpath.JsonPath
// import com.springbootstudy2024.springbootstudy2024.chapter7.main.model.Course
// import com.springbootstudy2024.springbootstudy2024.chapter7.main.service.CourseService
// import org.hamcrest.Matchers
// import org.junit.jupiter.api.Assertions
// import org.junit.jupiter.api.Test
// import org.junit.jupiter.api.extension.ExtendWith
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
// import org.springframework.boot.test.context.SpringBootTest
// import org.springframework.test.context.junit.jupiter.SpringExtension
// import org.springframework.test.web.servlet.MockMvc
// import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
// import org.springframework.test.web.servlet.result.MockMvcResultHandlers
// import org.springframework.test.web.servlet.result.MockMvcResultMatchers
//
// @SpringBootTest
// @AutoConfigureMockMvc
// @ExtendWith(SpringExtension::class) // JUnit 5의 주피터 프로그래밍 모델과 스프링 테스트 컨텍스트 프레임워크를 함께 테스트에 사용 가능
// internal class CourseTrackerApiApplicationTests(
//     private val courseService: CourseService,
//     private val mockMvc: MockMvc
// ) {
//
//     @Test
//     @Throws(Exception::class)
//     fun testPostCourse() {
//         val course = Course(
//             name = "Rapid Spring Boot Application Development",
//             category = "Spring",
//             rating = 5,
//             description = "Rapid Spring Boot Application Development",
//         )
//         val objectMapper = ObjectMapper()
//         val response = mockMvc.perform(
//             MockMvcRequestBuilders.post("/courses/")
//                 .contentType("application/json")
//                 .content(objectMapper.writeValueAsString(course)),
//         )
//             .andDo(MockMvcResultHandlers.print())
//             .andExpect(MockMvcResultMatchers.jsonPath("$.*", Matchers.hasSize<Any>(5)))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.greaterThan(0)))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Rapid Spring Boot Application Development"))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("Spring"))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.rating").value(5))
//             .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn().response
//         val id = JsonPath.parse(response.contentAsString).read<Int>("$.id")
//         Assertions.assertNotNull(courseService.getCourseById(id.toLong()))
//     }
//
//     @Test
//     @Throws(Exception::class)
//     fun testRetrieveCourse() {
//         val course = Course(
//             name = "Rapid Spring Boot Application Development",
//             category = "Spring",
//             rating = 5,
//             description = "Rapid Spring Boot Application Development",
//         )
//         val objectMapper = ObjectMapper()
//         val response = mockMvc!!.perform(
//             MockMvcRequestBuilders.post("/courses/")
//                 .contentType("application/json")
//                 .content(objectMapper.writeValueAsString(course)),
//         )
//             .andDo(MockMvcResultHandlers.print())
//             .andExpect(MockMvcResultMatchers.jsonPath("$.*", Matchers.hasSize<Any>(5)))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.greaterThan(0)))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Rapid Spring Boot Application Development"))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("Spring"))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.rating").value(5))
//             .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn().response
//         val id = JsonPath.parse(response.contentAsString).read<Int>("$.id")
//         mockMvc.perform(MockMvcRequestBuilders.get("/courses/{id}", id))
//             .andDo(MockMvcResultHandlers.print())
//             .andExpect(MockMvcResultMatchers.jsonPath("$.*", Matchers.hasSize<Any>(5)))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.greaterThan(0)))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Rapid Spring Boot Application Development"))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("Spring"))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.rating").value(5))
//             .andExpect(MockMvcResultMatchers.status().isOk())
//     }
//
//     @Test
//     @Throws(Exception::class)
//     fun testInvalidCouseId() {
//         mockMvc.perform(MockMvcRequestBuilders.get("/courses/{id}", 100))
//             .andDo(MockMvcResultHandlers.print())
//             .andExpect(MockMvcResultMatchers.status().isNotFound())
//     }
//
//     @Test
//     @Throws(Exception::class)
//     fun testUpdateCourse() {
//         val course = Course(
//             name = "Rapid Spring Boot Application Development",
//             category = "Spring",
//             rating = 3,
//             description = "Rapid Spring Boot Application Development",
//         )
//         val objectMapper = ObjectMapper()
//         val response = mockMvc!!.perform(
//             MockMvcRequestBuilders.post("/courses/")
//                 .contentType("application/json")
//                 .content(objectMapper.writeValueAsString(course)),
//         )
//             .andDo(MockMvcResultHandlers.print())
//             .andExpect(MockMvcResultMatchers.jsonPath("$.*", Matchers.hasSize<Any>(5)))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.greaterThan(0)))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Rapid Spring Boot Application Development"))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("Spring"))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.rating").value(3))
//             .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn().response
//         val id = JsonPath.parse(response.contentAsString).read<Int>("$.id")
//         val updatedCourse: Course =  Course(
//             name = "Rapid Spring Boot Application Development",
//             category = "Spring",
//             rating = 5,
//             description = "Rapid Spring Boot Application Development",
//         )
//         mockMvc.perform(
//             MockMvcRequestBuilders.put("/courses/{id}", id)
//                 .contentType("application/json")
//                 .content(objectMapper.writeValueAsString(updatedCourse)),
//         )
//             .andDo(MockMvcResultHandlers.print())
//             .andExpect(MockMvcResultMatchers.jsonPath("$.*", Matchers.hasSize<Any>(5)))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Rapid Spring Boot Application Development"))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("Spring"))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.rating").value(5))
//             .andExpect(MockMvcResultMatchers.status().isOk())
//     }
//
//     @Test
//     @Throws(Exception::class)
//     fun testDeleteCourse() {
//         val course = Course(
//             name = "Rapid Spring Boot Application Development",
//             category = "Spring",
//             rating = 5,
//             description = "Rapid Spring Boot Application Development",
//         )
//         val objectMapper = ObjectMapper()
//         val response = mockMvc.perform(
//             MockMvcRequestBuilders.post("/courses/")
//                 .contentType("application/json")
//                 .content(objectMapper.writeValueAsString(course)),
//         )
//             .andDo(MockMvcResultHandlers.print())
//             .andExpect(MockMvcResultMatchers.jsonPath("$.*", Matchers.hasSize<Any>(5)))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.greaterThan(0)))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Rapid Spring Boot Application Development"))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("Spring"))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.rating").value(5))
//             .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn().response
//         val id = JsonPath.parse(response.contentAsString).read<Int>("$.id")
//         mockMvc.perform(MockMvcRequestBuilders.delete("/courses/{id}", id))
//             .andDo(MockMvcResultHandlers.print())
//             .andExpect(MockMvcResultMatchers.status().isOk())
//     }
// }

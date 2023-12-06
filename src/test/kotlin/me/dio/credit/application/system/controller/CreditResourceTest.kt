package me.dio.credit.application.system.controller

import com.fasterxml.jackson.databind.ObjectMapper
import me.dio.credit.application.system.dto.request.CustomerDto
import me.dio.credit.application.system.dto.request.CustomerUpdateDto
import me.dio.credit.application.system.entity.Customer
import me.dio.credit.application.system.repository.CustomerRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.util.Random

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CreditResourceTest {
    @Autowired
    private lateinit var customerRepository: CustomerRepository
  
    @Autowired
    private lateinit var mockMvc: MockMvc
  
    @Autowired
    private lateinit var objectMapper: ObjectMapper
  
    companion object {
      const val URL: String = "/api/credits"
    }
  
    @BeforeEach
    fun setup() = customerRepository.deleteAll()
  
    @AfterEach
    fun tearDown() = customerRepository.deleteAll()
  
    @Test
    fun `should create a credit and return 201 status`(){
        //given
        val credit: creditDto = buildCredit()
        val valueAsString: String = objectMapper.writeValueAsString(credit)
    
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(valueAsString)
        )
        .andExpect(MockMvcResultMatchers.status().isCreated)
        .andExpect(MockMvcResultMatchers.jsonPath("$.creditValue").value("500.0"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.dayFirstInstallment").value("22/04/2023"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfInstallments").value("5"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.customer.firstName").value("Cami"))
      

    }


    private fun buildCredit(
    creditValue: BigDecimal = BigDecimal.valueOf(500.0),
    dayFirstInstallment: LocalDate = LocalDate.of(2023, Month.APRIL, 22),
    numberOfInstallments: Int = 5,
    customer: Customer
    ): Credit = Credit(
    creditValue = creditValue,
    dayFirstInstallment = dayFirstInstallment,
    numberOfInstallments = numberOfInstallments,
    customer = customer
    )

    private fun buildCustomer(
    firstName: String = "Cami",
    lastName: String = "Cavalcante",
    cpf: String = "28475934625",
    email: String = "camila@gmail.com",
    password: String = "12345",
    zipCode: String = "12345",
    street: String = "Rua da Cami",
    income: BigDecimal = BigDecimal.valueOf(1000.0),
    ) = Customer(
    firstName = firstName,
    lastName = lastName,
    cpf = cpf,
    email = email,
    password = password,
    address = Address(
        zipCode = zipCode,
        street = street,
    ),
    income = income,
    )

}
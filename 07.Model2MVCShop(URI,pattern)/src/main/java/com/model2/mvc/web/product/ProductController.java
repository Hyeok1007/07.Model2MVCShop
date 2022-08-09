package com.model2.mvc.web.product;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;

@Controller
@RequestMapping("/product/*")
public class ProductController {

	
	///Filed
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	
	public ProductController() {
		System.out.println(this.getClass());
	}
	
	@Value("#{commonProperties['pageUnit']}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	int pageSize;
	
	//@RequestMapping("/addProductView.do")
	@RequestMapping(value="addProduct", method=RequestMethod.GET)
	public String addProduct() throws Exception { 
		
		System.out.println("/product/addProduct : GET");
		
		return "redirect:/product/addProductView.jsp";
	}

	//@RequestMapping("/addProduct.do")
	@RequestMapping(value="addProduct", method=RequestMethod.POST)
	public String addProduct(@ModelAttribute("product") Product product) throws Exception {
		
		System.out.println("/product/addProduct : POST");
		//Business Logic
		productService.addProduct(product);
		
		return "forward:/product/addProduct.jsp";
	}
	
	//@RequestMapping(value="/getProduct.do")
	@RequestMapping(value="getProduct", method=RequestMethod.GET)
	public String getProduct(@RequestParam("prodNo") String prodNo, Model model) throws Exception {
		
		System.out.println("/product/getProduct : GET");

		// Business Logic
		Product product = productService.getProduct(Integer.parseInt(prodNo));
		// Model과 View 연결
		System.out.println("MODEL VIEW 연결 전");
		
		model.addAttribute("product",product);
				
		System.out.println("모델과 뷰 연결 되었나?");
		return "forward:/product/getProduct.jsp";
	}
	

//	@RequestMapping("/updateProductView.do")
//	public String updateProductView(@RequestParam("prodNo") int prodNo, Model model) throws Exception {
//		
//		System.out.println("/updateProductView.do");
//		//Business Logic
//		Product product = productService.getProduct(prodNo);
//		//Model 과 View 연결
//		model.addAttribute("product", product);
//		
//		return "forward:/product/updateProductView.jsp";
//	}
	
//	@RequestMapping("/updateProductView.do")
	@RequestMapping(value="updateProduct", method=RequestMethod.GET)
	public String updateProduct(@RequestParam("prodNo") String prodNo, Model model) throws Exception {
		
		System.out.println("/product/updateProduct : GET");
		//Business Logic
		Product product = productService.getProduct(Integer.parseInt(prodNo));
		//Model 과 View 연결
		model.addAttribute("product", product);
		
//		return "forward:/product/updateProductView.jsp";
		return "forward:/product/updateProductView.jsp";
	}
	
//	@RequestMapping("/updateProduct.do")
	@RequestMapping(value="updateProduct", method=RequestMethod.POST)
	public String updateProduct(@ModelAttribute("product") Product product, Model model, HttpSession session) throws Exception {
		
		System.out.println("/product/updateProduct : POST");
		//Business Logic
		productService.updateProduct(product);
				
		
//		String sessionId = ((Product)session.getAttribute("product")).getProdNo();
//		session.setAttribute("product", product);
		model.addAttribute("product",product);
		
//		return "redirect:/getProduct.do?prodNo="+product.getProdNo();
//		return "redirect:/getProduct?prodNo="+product.getProdNo();
		return "redirect:/product/getProduct?prodNo="+product.getProdNo();
	}
	
//	@RequestMapping("/listProduct.do")
	@RequestMapping(value="listProduct")
	public String listProduct(@ModelAttribute("search") Search search, Model model, HttpServletRequest request) throws Exception {
		
		System.out.println("/product/listProduct : GET / POST");
		
		if(search.getCurrentPage()==0) {
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		//Business login 수행
		Map<String,Object> map = productService.getProductList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")). intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		//Model 과 View 연결
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		
		return "forward:/product/listProduct.jsp";
	}
	
}

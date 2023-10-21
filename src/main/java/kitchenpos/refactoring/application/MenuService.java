package kitchenpos.refactoring.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.refactoring.application.dto.MenuProductRequest;
import kitchenpos.refactoring.application.dto.MenuRequest;
import kitchenpos.refactoring.application.dto.MenuResponse;
import kitchenpos.refactoring.domain.Menu;
import kitchenpos.refactoring.domain.MenuGroup;
import kitchenpos.refactoring.domain.MenuGroupRepository;
import kitchenpos.refactoring.domain.MenuProduct;
import kitchenpos.refactoring.domain.MenuRepository;
import kitchenpos.refactoring.domain.Price;
import kitchenpos.refactoring.domain.Product;
import kitchenpos.refactoring.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        Price price = new Price(menuRequest.getPrice());
        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);
        List<MenuProduct> menuProducts = getMenuProducts(menuRequest);
        List<Product> products = getProducts(menuRequest);

        Menu menu = Menu.create(
                menuRequest.getName(),
                price,
                menuGroup,
                menuProducts,
                products
        );

        Menu savedMenu = menuRepository.save(menu);

        return MenuResponse.from(savedMenu);
    }

    private List<MenuProduct> getMenuProducts(MenuRequest menuRequest) {
        return menuRequest.getMenuProducts().stream()
                .map(MenuProductRequest::toMenuProduct)
                .collect(Collectors.toList());
    }

    private List<Product> getProducts(MenuRequest menuRequest) {
        List<Long> productIds = menuRequest.getMenuProducts().stream()
                .map(MenuProductRequest::getProductId)
                .collect(Collectors.toList());

        return productRepository.findAllByIdIn(productIds);
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
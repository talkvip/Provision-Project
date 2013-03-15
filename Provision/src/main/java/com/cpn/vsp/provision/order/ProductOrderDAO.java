package com.cpn.vsp.provision.order;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cpn.vsp.provision.database.AbstractDAO;

@Service
@Transactional
public class ProductOrderDAO extends AbstractDAO<ProductOrder> {

	@Override
	public Class<ProductOrder> getDTOClass() {
		return ProductOrder.class;
	}
}

package hello.project.repository.buy;

import hello.project.domain.buy.CTR;
import hello.project.domain.buy.orderDetail.OrderDetail;
import hello.project.domain.buy.orderList.OrderList;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

public interface BuyRepository {

    void ListSave(OrderList orderList);

    Optional<OrderList> ListfindById(Long listId);

    List<OrderList> ListfindByUserId(Long userId);

    void ListDelete(Long listId);

    void ListUpdate(Long listId, OrderList orderList);


    void DetailSave(OrderDetail orderDetail);

    Optional<OrderDetail> DetailfindById(Long detailId);

    List<OrderDetail> DetailfindByListId(Long orderListId);

    List<OrderDetail> DetailfindByOptionId(Long itemOptionId);

    void DetailDelete(Long detailId);


    Integer countId();

    Integer countUserId();

    Integer sumPrice();

    CTR countCTR();

    void updateCTR(Long ctr);
}

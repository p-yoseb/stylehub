package hello.project.repository.buy.mybatisBuy;

import hello.project.domain.buy.CTR;
import hello.project.domain.buy.orderDetail.OrderDetail;
import hello.project.domain.buy.orderList.OrderList;
import hello.project.repository.buy.BuyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MyBatisBuyRepository implements BuyRepository {
    private final BuyMapper buyMapper;

    @Override
    public void ListSave(OrderList orderList) {
        buyMapper.ListSave(orderList);
    }

    @Override
    public Optional<OrderList> ListfindById(Long listId) {
        return buyMapper.ListfindById(listId);
    }

    @Override
    public List<OrderList> ListfindByUserId(Long userId) {
        return buyMapper.ListfindByUserId(userId);
    }

    @Override
    public void ListDelete(Long listId) {
        buyMapper.ListDelete(listId);
    }

    @Override
    public void ListUpdate(Long listId, OrderList orderList) {
        buyMapper.ListUpdate(listId, orderList);
    }

    @Override
    public void DetailSave(OrderDetail orderDetail) {
        buyMapper.DetailSave(orderDetail);
    }

    @Override
    public Optional<OrderDetail> DetailfindById(Long detailId) {
        return buyMapper.DetailfindById(detailId);
    }

    @Override
    public List<OrderDetail> DetailfindByListId(Long orderListId) {
        return buyMapper.DetailfindByListId(orderListId);
    }

    @Override
    public List<OrderDetail> DetailfindByOptionId(Long itemOptionId) {
        return buyMapper.DetailfindByOptionId(itemOptionId);
    }

    @Override
    public void DetailDelete(Long detailId) {
        buyMapper.DetailDelete(detailId);
    }


    @Override
    public Integer countId() {
        return buyMapper.countId();
    }

    @Override
    public Integer countUserId() {
        return buyMapper.countUserId();
    }

    @Override
    public Integer sumPrice() {
        return buyMapper.sumPrice();
    }

    @Override
    public CTR countCTR() {
        return buyMapper.countCTR();
    }

    @Override
    public void updateCTR(Long ctr) {
        buyMapper.updateCTR(ctr);
    }
}

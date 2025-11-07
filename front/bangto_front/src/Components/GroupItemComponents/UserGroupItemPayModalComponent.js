import { useEffect } from "react";
import Modal from "react-modal";
import axios from "axios";
import { useNavigate } from "react-router-dom";
Modal.setAppElement("#root");

const UserGroupItemPayModalComponent = (props) => {
  const params = new URLSearchParams(window.location.search);
  const amount = params.get("amount");
  const navigate = useNavigate();
  useEffect(() => {
    console.log(props.payModal);
  }, [props.payModal]);

  const handlePayGroupItem = async () => {
    try {
      if (props.address !== undefined) {
        const groupItem = JSON.parse(localStorage.getItem("group-item"));
        const response = await axios.post(
          `${process.env.REACT_APP_BACKEND_SERVER_PORT}/group-pay/pay`,
          {
            groupItemPk: groupItem.id,
            optionPk: groupItem.option.id,
            amount,
            address: props.address,
            itemPk: groupItem.item.id,
          },
          {
            withCredentials: true,
            headers: {
              Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
          }
        );
        if (response.status == 200) {
          alert("결제 성공");
          props.setPayModal(false);
          navigate("/user");
        }
      } else {
        alert("주소 입력 오류");
      }
    } catch (err) {
      console.log(err);
      alert("결제 실패");
    }
  };

  return props.payModal !== undefined &&
    props.setPayModal !== undefined &&
    props.payModal &&
    props.wallet ? (
    <Modal
      isOpen={props.payModal}
      onRequestClose={() => props.setPayModal(false)}
      className="modal_User_Pay"
      overlayClassName="modal_User_Pay_Overlay"
    >
      <table className="User_Pay_Cash">
        <tr>
          <td>현재 보유 캐시</td>
          <td>{props.wallet.cash}원</td>
        </tr>
        <tr>
          <td>결제 후 캐시</td>
          <td>{props.wallet.cash - props.totalCost}원</td>
        </tr>
          {props.wallet.cash - props.totalCost > 0 ? (
            <tr>
              <td>배송비 정보</td>
              <td style={{display : "flex", flexDirection: "column"}}>
                <div>기본 5000 원</div>
                <div>30000원 이상 2500 원</div>
                <div>50000원 이상 0 원</div>
              </td>
            </tr>
          ) : (
            <td>잔액이 부족합니다.</td>
          )}
      </table>
      <div style={{width : "100%"}}>
        <button className="btn_User_Pay" onClick={() => handlePayGroupItem()}>
          결제완료
        </button>
        <button
          className="btn_User_Pay"
          onClick={() => props.setPayModal(false)}
        >
          결제취소
        </button>
      </div>
    </Modal>
  ) : null;
};

export default UserGroupItemPayModalComponent;

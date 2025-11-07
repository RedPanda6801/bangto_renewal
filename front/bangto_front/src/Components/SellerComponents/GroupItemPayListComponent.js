import { use, useEffect, useState } from "react";
import "../StoreComponent.css";
import { resContent } from "../UtilComponent/ResponseData";
import axios from "axios";
import { DeliverType } from "../UtilComponent/DataFormat";

const GroupItemPayListComponent = (props) => {
  const [payList, setPayList] = useState([]);
  const [selectPayList, setSelectPayList] = useState([]);

  useEffect(() => {
    handleGetPayList();
  }, [props.store]);

  const handleGetPayList = () => {
    console.log(props.store);
    axios
      .get(
        `${process.env.REACT_APP_BACKEND_SERVER_PORT}/group-pay/store/get-list`,
        {
          params: { storePk: props.store },
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      )
      .then((res) => {
        setPayList(resContent(res));
        console.log(resContent(res));
      })
      .catch((err) => console.log(err));
  };

  const handleDeliveringSingle = async (id) => {
    try {
      const response = await axios.post(
        `${process.env.REACT_APP_BACKEND_SERVER_PORT}/group-pay/delivering-check`,
        { id },
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );
      if (response.status == 200) {
        alert("배송 처리 완료");
        handleGetPayList();
      }
    } catch (err) {
      console.log(err);
      alert("단일 배송 실패");
    }
  };

  const handleDeliveredSingle = async (id) => {
    try {
      const response = await axios.post(
        `${process.env.REACT_APP_BACKEND_SERVER_PORT}/group-pay/delivered-check`,
        { id },
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );
      if (response.status == 200) {
        alert("배송 완료 처리 완료");
        handleGetPayList();
      }
    } catch (err) {
      console.log(err);
      alert("배송 완료 처리 실패");
    }
  };

  const handleSetSelectPayList = (e, id) => {
    if (e.target.checked) {
      // 체크하면 추가
      setSelectPayList((prev) => [...prev, id]);
    } else {
      // 체크 해제하면 제거
      setSelectPayList((prev) => prev.filter((payId) => payId !== id));
    }
  };

  const handleTotalPay = async () => {
    if (
      window.confirm(
        "선택한 항목에 대해 일괄 배송을 합니다.\n *이후의 불이익에 책임지지 않습니다.*"
      )
    ) {
      try {
        const response = await axios.post(
          `${process.env.REACT_APP_BACKEND_SERVER_PORT}/group-pay/delivering-check-total`,
          { payIdList: selectPayList },
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
          }
        );
        if (response.status == 200) {
          alert("배송 완료 처리 완료");
          setSelectPayList([]);
          handleGetPayList();
        }
      } catch (err) {
        console.log(err);
        alert("배송 완료 처리 실패");
      }
    }
  };

  return (
    <div className="box_Group_Detail">
      {payList && payList.length > 0 ? (
        <>
          <table>
            <tr>
              <th>
                <div>{"상품명 (옵션정보)"}</div>
              </th>
              <th>선택</th>
              <th>결제 비용</th>
              <th>결제 날짜</th>
              <th>배송지</th>
              <th>배송 상태</th>
              <th>단일 배송</th>
              <th>배송 완료</th>
            </tr>
        {
          payList.map((pay) => (
                <tr>
                  <td>
                    <div className="checkbox">
                      <input
                        type="checkbox"
                        checked={selectPayList.includes(pay.id)}
                        onChange={(e) => handleSetSelectPayList(e, pay.id)}
                      />
                    </div>
                  </td>
                  <td>
                    <div>{`${pay.itemTitle} - (${pay.optionInfo} / ${pay.amount}개)`}</div>
                  </td>
                  <td>{pay.pay}원</td>
                  <td>{pay.soldDate}</td>
                  <td>{pay.address}</td>
                  <td>{DeliverType[pay.deliverInfo]}</td>
                  <td>
                    <button
                      onClick={() => handleDeliveringSingle(pay.id)}
                      disabled={pay.deliverInfo !== "Preparing"}
                    >
                      단일 배송
                    </button>
                  </td>
                  <td>
                    <button
                      onClick={() => handleDeliveredSingle(pay.id)}
                      disabled={pay.deliverInfo !== "Delivering"}
                    >
                      배송 완료
                    </button>
                  </td>
                </tr>
            ))}
          </table>
          <br/>
          <button style={{ width: "100%" }} onClick={handleTotalPay}>
            일괄 배송
          </button>
        </>
      )
      : (
        <div>결제 목록 없음.</div>
      )}
    </div>
  );
};

export default GroupItemPayListComponent;

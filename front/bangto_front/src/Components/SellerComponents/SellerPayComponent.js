import axios from "axios";
import { useEffect, useState } from "react";
import { data, Navigate, useNavigate } from "react-router-dom";
import { resContent, resPage } from "../UtilComponent/ResponseData";
import { DeliverType } from "../UtilComponent/DataFormat";
import PagenationComponent from "../UtilComponent/PagenationComponent";

const SellerPayComponent = (props) => {
  const [page, setPage] = useState(1);
  const [totalPage, setTotalPage] = useState(1);
  const [payObject, setPayObject] = useState({}); // [ soldDate1 : {}, soldDate2 : {}, ...]
  const [soldDate, setSoldDate] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    handleGetPayList();
  }, [props.store, page]);

  const handleGetPayList = () => {
    console.log(props.store);
    axios
      .get(
        `${process.env.REACT_APP_BACKEND_SERVER_PORT}/pay/get-my-store-info/${props.store}/${page}`,
        {
          params: { storePk: props.store },
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      )
      .then((res) => {
        const dataList = resContent(res);
        if(dataList.length == 0){setPayObject([]); return;}
        const soldDateList = [];
        let newObj = payObject;
        if (Array.isArray(dataList)) {
          dataList.forEach((data) => {
            if (!Object.keys(newObj).includes(data.soldDate)) {
              newObj[data.soldDate] = [data];
              soldDateList.push(data.soldDate);
            } else {
              newObj[data.soldDate].push(data);
            }
          });
        }
        setPayObject(newObj);
        setSoldDate([...soldDateList]);
        setTotalPage(resPage(res).totalPages);
      })
      .catch((err) => {console.log(err)});
  };

  const handleDeilver = (payList, deliverInfo) => {
    try {
      if (Array.isArray(payList)) {
        payList.forEach(async (pay) => {
          await axios.post(
            `${process.env.REACT_APP_BACKEND_SERVER_PORT}/pay/modify`,
            { id: pay.id, deliverInfo },
            {
              headers: {
                Authorization: `Bearer ${localStorage.getItem("token")}`,
              },
            }
          );

          // if (response.status == 200) {
          //   alert("배송 처리 완료");
          //   navigate(0);
          // }
        });
        alert("배송 처리 완료");
        navigate(0);
      }
    } catch (err) {
      alert("배송 처리 실패");
      // navigate(0);
      console.log("에러 발생");
      console.log(err);
    }
  };

  return (
    <div className="box_Sold_Detail">
      {payObject !== undefined && Object.keys(payObject).length > 0 ? (
        <table>
          <tr>
            <th>상품 목록</th>
            <th>총 결제 내역</th>
            <th>결제 날짜</th>
            <th>배송 상태</th>
            <th>배송</th>
            <th>배송 완료</th>
          </tr>
          {soldDate ? (
            soldDate.map((date) =>
              date ? (
                Array.isArray(payObject[date]) ? (
                  <tr>
                    <td>
                      {payObject[date].map((pay) => (
                        <div>
                          {`${pay.itemName} - (${pay.optionInfo} / ${pay.amount}개)`}
                        </div>
                      ))}
                    </td>
                    <td>
                      {payObject[date].reduce(
                        (sum, pay) => sum + pay.soldPrice,
                        0
                      )}
                      원
                    </td>
                    <td>{date}</td>
                    <td>{DeliverType[payObject[date][0].deliverInfo]}</td>
                    <td>
                      <button
                        onClick={() =>
                          handleDeilver(payObject[date], "Delivering")
                        }
                        disabled={payObject[date][0].deliverInfo != "Preparing"}
                      >
                        배송
                      </button>
                    </td>
                    <td>
                      <button
                        onClick={() =>
                          handleDeilver(payObject[date], "Delivered")
                        }
                        disabled={
                          payObject[date][0].deliverInfo != "Delivering"
                        }
                      >
                        배송 완료
                      </button>
                    </td>
                  </tr>
                ) : (
                  <div></div>
                )
              ) : (
                <div></div>
              )
            )
          ) : (
            <div></div>
          )}
        </table>
      ) : (
        <div>판매 정보 없음</div>
      )}
      <PagenationComponent page={page} setPage={setPage} totalPage={totalPage}/>
    </div>
  );
};

export default SellerPayComponent;

import axios from "axios";
import { useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import { resContent } from "../UtilComponent/ResponseData";
import { DeliverType } from "../UtilComponent/DataFormat";
import UserCommentAddComponent from "./UserCommentAddComponent";
import "./UserPaylistComponent.css";
import PagenationComponent from "../UtilComponent/PagenationComponent";

const UserPayListComponent = () => {
  const [commentModal, setCommentModal] = useState(false);
  const [selectYear, setSelectYear] = useState({
    2025: true,
    2024: false,
    2023: false,
    2022: false,
  });
  const [year, setYear] = useState("2025");
  const [nowPage, setNowPage] = useState(1);
  const [totalPage, setTotalPage] = useState(1);
  const [payList, setPayList] = useState([]);
  const [groupPayList, setGroupPayList] = useState([]);
  const [wallet, setWallet] = useState({});
  const [payId, setPayId] = useState("");
  const navigate = useNavigate();
  const [user, setUser] = useState("");
  const [openOrder, setOpenOrder] = useState(true);
  
  useEffect(() => {
    axios
      .get(`${process.env.REACT_APP_BACKEND_SERVER_PORT}/wallet/my/get-info`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      })
      .then((res) => {
        const wallet = resContent(res);
        setUser(wallet.user.name);
        setWallet(wallet);
        handleGetPayList();
      })
      .catch((err) => {
        console.log(err);
        alert("로그인 오류");
        navigate(-1);
      });
  }, []);

  useEffect(() => {
    handleGetPayList();
    hnadleGetGroupPayList();
  }, [year]);

  const handleGetPayList = async () => {
    try {
      const response = await axios.get(
        `${process.env.REACT_APP_BACKEND_SERVER_PORT}/pay/get-info/${year}/${nowPage}`,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );
      if (response.status == 200) {
        setPayList(resContent(response));
        setTotalPage(resContent(response).totalPages);
      }
    } catch (err) {
      console.log(err);
      if (err.response.data == "결제 내역이 없습니다.") {
        setPayList([]);
      } else {
        alert("결제 정보 불러오기 오류");
      }
    }
  };

  const hnadleGetGroupPayList = async () => {
    try {
      const response = await axios.get(
        `${process.env.REACT_APP_BACKEND_SERVER_PORT}/group-pay/my/get-list/${year}`,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );
      if (response.status == 200) {
        console.log(response);
        setGroupPayList(resContent(response));
      }
    } catch (err) {
      console.log(err);
      if (err.response.data == "결제 내역이 없습니다.") {
        setPayList([]);
      } else {
        alert("결제 정보 불러오기 오류");
      }
    }
  };

  const handleSelectYear = (curYear) => {
    const newYearObj = {};
    const yearList = Object.keys(selectYear);
    yearList.forEach((year) => {
      console.log(year, curYear);
      if (year == curYear) {
        newYearObj[year] = true;
        setYear(year);
      } else {
        newYearObj[year] = false;
      }
    });
    setSelectYear(newYearObj);
  };

  const handleOpenCommentModal = (id) => {
    setCommentModal(true);
    setPayId(id);
  };

  return (
    <>
      <div className="box_User_Paid">
        <div className="box_User_Cash">
          <table className="table_User_Cash">
            {wallet && Object.keys(wallet).length > 0 ? (
              <tr>
                <td>캐시</td>
                <td>{wallet.cash} 원</td>
                <td>캐시백</td>
                <td>{wallet.cashBack} 원</td>
              </tr>
            ) : (
              <div>캐시 조회 불가</div>
            )}
          </table>
        </div>
      </div>
      <div className="order_select_div">
      <h2 className="order_select_button" onClick={()=> setOpenOrder(true)}>주문 내역</h2>
      <h2 className="order_select_button" onClick={()=> setOpenOrder(false)}>공동 구매 주문 내역</h2>
      </div>
      <br></br>
      <div className="box_User_List">
        <button
          className={`btn_sort_list ${
            selectYear["2025"] ? "btn_sort_list_selected" : ""
          }`}
          onClick={() => handleSelectYear("2025")}
        >
          2025
        </button>
        <button
          className={`btn_sort_list ${
            selectYear["2024"] ? "btn_sort_list_selected" : ""
          }`}
          onClick={() => handleSelectYear("2024")}
        >
          2024
        </button>
        <button
          className={`btn_sort_list ${
            selectYear["2023"] ? "btn_sort_list_selected" : ""
          }`}
          onClick={() => handleSelectYear("2023")}
        >
          2023
        </button>
        <button
          className={`btn_sort_list ${
            selectYear["2022"] ? "btn_sort_list_selected" : ""
          }`}
          onClick={() => handleSelectYear("2022")}
        >
          2022
        </button>
      </div>
      <>
      {openOrder? (
         <div className="box_group_pay">
           {payList && payList.length > 0 ? (
             payList.map((pay) => (
               <table className="table_User_Paid">
                 <tr>
                   <td colSpan={4}>{pay.soldDate}</td>
                   <td>{DeliverType[pay.deliverInfo]}</td>
                 </tr>
                 <tr>
                   <td colSpan={4}>{pay.itemTitle}</td>
                 </tr>
                 <tr>
                   <td>옵션 : {pay.optionInfo}</td>
                   <td>개수 : {pay.amount}</td>
                   <td>결제금액 : {pay.soldPrice} 원</td>
                   <td>
                     <button onClick={() => handleOpenCommentModal(pay.id)}>
                       후기작성
                     </button>
                   </td>
                 </tr>
               </table>
             ))
           ) : (
             <div>결제 내역 없음.</div>
           )}
          <PagenationComponent page={nowPage} setPage={setNowPage} totalPage={totalPage}/>
         <UserCommentAddComponent
           modal={commentModal}
           setModal={setCommentModal}
           name={user}
           id={payId}
         ></UserCommentAddComponent>
       </div>
      ): (
        <div className="box_group_pay">
          {groupPayList && groupPayList.length > 0 ? (
            groupPayList.map((pay) => (
              <table className="table_User_Paid">
                <tr>
                  <td colSpan={4}>{pay.soldDate}</td>
                  <td>{DeliverType[pay.deliverInfo]}</td>
                </tr>
                <tr>
                  <td colSpan={4}>{pay.itemTitle}</td>
                </tr>
                <tr>
                  <td>옵션 : {pay.optionInfo}</td>
                  <td>개수 : {pay.amount}</td>
                  <td>결제금액 : {pay.pay} 원</td>
                </tr>
              </table>
            ))
          ) : (
            <div>결제 내역 없음.</div>
          )}
        </div>
      )}
      </>
    </>
  );
};

export default UserPayListComponent;

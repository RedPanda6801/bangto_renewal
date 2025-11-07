import { useEffect, useState } from "react";
import axios from "axios";
import { resPage , resContent } from '../UtilComponent/ResponseData';
import { useNavigate } from 'react-router-dom';
import {TimeStamp} from "../UtilComponent/DataFormat";
import PagenationComponent from "../UtilComponent/PagenationComponent";

const ManagerApplyComponent = () => {
    const [applyPage, setApplyPage] = useState(1);
    const [applyTotalPage, setApplyTotalPage] = useState(1);
    const [applyList, setApplyList] = useState([]);
    const [selectedApply, setSelectedApply] = useState(""); 
    const navigate = useNavigate();

    useEffect(() => {handleApplyList()},[applyPage])
    const SERVER_PORT = process.env.REACT_APP_BACKEND_SERVER_PORT;

    const handleApplyList = () => {
        axios.get(`${SERVER_PORT}/manager/apply/get-list/${applyPage}`, {
            withCredentials : true,
            headers: { "Authorization": `Bearer ${localStorage.getItem("token")}`,
        }}).then((res)=> {
            setApplyList(resContent(res));
            setApplyPage(applyPage);
            setApplyTotalPage(resPage(res) ? resPage(res).totalPages : 1);
        }).catch((error) => {
            if(error.status === 401){
                alert("토큰 만료 오류 : 다시 로그인하세요.");
                navigate("/", { state: { category: "Main" } });
            }
            console.error("회원 정보 가져오기 오류:", error);
        })
    }

    const handleApplyProcess = async (process, applyId) => {
        try{
            const response = await axios.post(`${SERVER_PORT}/manager/apply/modify`, 
                {process, sellerAuthPk : applyId}, {
                withCredentials : true,
                headers: { "Authorization": `Bearer ${localStorage.getItem("token")}`,
            }})

            if(response.status == 200){
                alert("처리되었습니다.");
                handleApplyList();
            }
        }catch(error){
            console.log(error);
            alert("신청서 처리 실패");
        }
    }

    return <div className="box_Manager_StoreAuth_Detail">
                <div className="box_Manager_StoreAuth_Info">
                    <table className="table_Manager_StoreAuth_Info">
                        <tr>
                            <th>판매자 이름</th>
                            <th>매장 이름</th>
                            <th>사업자 번호</th>
                            <th>지원 날짜</th>
                            <th>승인 여부</th>
                            <th>승인 / 거절</th>
                        </tr>
                        {applyList && Array.isArray(applyList) ? 
                            applyList.map(apply => (
                                <tr key={apply.id}>
                                    <td>{apply.userName}</td>
                                    <td>{apply.storeName}</td>
                                    <td>{apply.busiNum}</td>
                                    <td>{TimeStamp(apply.applyDate)}</td>
                                    <td>{apply.auth}</td>
                                    <td>
                                        <button onClick={() => handleApplyProcess("Accepted", apply.id)} disabled={apply.auth !== "Processing"}>승인</button>
                                        <button onClick={()=> handleApplyProcess("Duplicated", apply.id)} disabled={apply.auth !== "Processing"}>거부</button>
                                    </td>
                                </tr>
                            )) : <div>매장 없음</div>
                        }
                    </table>
                </div>
                <div>
                <PagenationComponent page={applyPage} setPage={setApplyPage} totalPage={applyTotalPage}/>
            </div>
            </div>
}

export default ManagerApplyComponent;
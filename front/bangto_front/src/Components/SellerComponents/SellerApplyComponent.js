import { use, useEffect, useState } from "react";
import axios from "axios";
import { resPage , resContent } from '../UtilComponent/ResponseData';
import { useNavigate } from 'react-router-dom';
import '../StoreComponent.css'; 

const SellerApplyComponent = () => {
    const [storeName, setStoreName] = useState("");
    const [busi, setBusi] = useState("");
    const navigate = useNavigate();
    const SERVER_PORT = process.env.REACT_APP_BACKEND_SERVER_PORT;

      // 필수 입력 항목 확인
    const isFormValid = storeName && busi;

    // 입력 항목 유효성 검사
    const handleStoreAuth = async () => {    
        // 유효성 검사 기준
        const validChar = /^[a-zA-Z가-힣0-9]+$/;
        const busiChar = /^\d{3}-\d{2}-\d{5}$/;

        let isStoreValid = false;
        let isBusiValid = false;
    
        // 유효성 검사
        if( 0 < storeName.length  && 11 > storeName.length  && validChar.test(storeName)) isStoreValid = true;
        if( busiChar.test(busi) )                                               isBusiValid = true;

        // 유효성 검사 결과
        if(!isStoreValid)  { alert("매장 이름 입력이 잘못되었습니다. (1 ~ 10자로 입력 가능, 특수문자 입력 불가)");}   
        else if(!isBusiValid)   { alert("사업자 번호 입력이 잘못되었습니다. (xxx-xx-xxxxx로 구성된 10자리 숫자)");}
        else if(isStoreValid && isBusiValid) {
        try { 
            const response = await axios.post(`${SERVER_PORT}/apply`, 
                {name : storeName, busiNum : busi},
                {withCredentials : true, headers: { "Authorization": `Bearer ${localStorage.getItem("token")}` }});  

            if (response.status == 200) {
            alert("판매자 인증 신청이 완료되었습니다."); 
            navigate(-1);
            } else {
            console.error("판매자 인증 신청 실패", response.data);
            }       
        } catch (error) {
            console.error("판매자 인증 신청 오류:", error);
            if (error.response) {
            console.error("서버 응답 오류:", error.response.data);
            }
        }
        }
    };

    return (
        <div className="box_Storeauth_Apply">
            <table>
            <tr>
                <td>
                <input
                    className="Auth_Store_name"
                    type="text"
                    placeholder="* 매장 이름"
                    value={storeName}
                    onChange={(e) => setStoreName(e.target.value)}/>
                </td>
            </tr>
            </table>
            <table>
            <tr>
                <td>
                <input
                    className="Auth_Busi_Num"
                    type="text"
                    placeholder="* 사업자 번호"
                    value={busi}
                    onChange={(e) => setBusi(e.target.value)}
                    onKeyDown={(e) => { if (e.key === 'Enter') { handleStoreAuth(); } }}/>
                </td>
            </tr>
            </table>
            <input 
            className="btn_Store_Auth"
            disabled={!isFormValid}
            type="button" 
            value="인증 신청"
            onClick={handleStoreAuth}/>
            {!isFormValid && <p className="StoreAuth_Ck_Error_Message">* 필수 입력 항목을 작성해주세요</p>}
        </div>
    );
}

export default SellerApplyComponent;
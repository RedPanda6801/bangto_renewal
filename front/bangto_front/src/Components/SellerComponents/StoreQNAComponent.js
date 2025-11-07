import { useEffect, useState } from "react";
import axios from "axios";
import { resContent, resPage } from "../UtilComponent/ResponseData";
import PagenationComponent from "../UtilComponent/PagenationComponent";

const StoreQNAComponent = (props) => {
    const [qnalist, setQnaList] = useState([]);
    const [page, setPage] = useState(1);
    const [totalPage, setTotalPage] = useState(1);
    const [answer, setAnswer] = useState("");
    const SERVER_PORT = process.env.REACT_APP_BACKEND_SERVER_PORT;

    useEffect(() => {
        handleQNAList();
    }, [props.storeInfo])

    const handleQNAList = async () => {
        try{
            const response = await axios.get(`${SERVER_PORT}/qna/store/get-list/${page}`, {
                params: { storePk: props.storeInfo.id }, // ⬅ 쿼리 파라미터로 전달
                headers: {
                  "Authorization": `Bearer ${localStorage.getItem("token")}`,
                }
            })
            if(response.status == 200){
                setQnaList(resContent(response));
                setTotalPage(resPage(response).totalPages);
            }
        }catch(err){
            console.log(err)
        }
    }
    
    const handleAddAnswer = async(itemPk, id) => {
        try{
            console.log(answer, itemPk, id)
            const response = await axios.post(`${SERVER_PORT}/qna/add-answer`, {
                acontent :answer, itemPk, id
            }, {
                headers: {
                  "Authorization": `Bearer ${localStorage.getItem("token")}`,
            }})

            if(response.status == 200){
                alert("답변 추가");
                handleQNAList();
            }
        }catch(err){
            console.log(err)
        }
    }

    return (
        <div className="box_QNA_Detail">              
            <table>
                <tr>
                    <th>상품명</th>
                    <th>유저 이름</th>
                    <th>질문 내용</th>
                    <th>답변 내용</th>
                </tr>
                {qnalist && qnalist.length > 0 ? 
                    qnalist.map((qna)=>(
                        <tr>
                            <td>{qna.itemTitle}</td>
                            <td>{qna.userName}</td>
                            <td>{qna.qcontent}<br></br>({qna.qwriteDate})</td>
                            {qna.acontent && qna.awriteDate ?
                                <td>{qna.acontent}<br></br>({qna.awriteDate})</td>
                                : <td>
                                    <textarea placeholder="답변을 작성해주세요" onChange={(e)=> setAnswer(e.target.value)}></textarea>
                                    <button onClick={()=> handleAddAnswer(qna.itemPk, qna.id)}>답변하기</button>
                                    </td>
                            } 
                        </tr>
                    )):<div></div>}
            </table>
            <PagenationComponent page={page} setPage={setPage} totalPage={totalPage}></PagenationComponent>
        </div>
    )
}

export default StoreQNAComponent;
package com.github.hjkim27.mapper.first;

import com.github.hjkim27.bean.dto.project.GhCommitDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GhCommitMapper {

    /**
     * <pre>
     *     commit 메시지 추가
     * </pre>
     *
     * @param ghCommitDTO ghCommit 정보
     */
    void insertRow(GhCommitDTO ghCommitDTO);

    /**
     * <pre>
     *     commit 메시지 업데이트
     *     - set : name, description, color, active(true)
     *     - where : label_id
     * </pre>
     *
     * @param ghCommitDTO ghCommit 정보
     */
    void updateRow(GhCommitDTO ghCommitDTO);

    /**
     * <pre>
     *     commit 메시지가 존재하는지 확인
     *     where : sha
     * </pre>
     *
     * @param ghCommitDTO ghCommit 정보
     * @return check Exist
     */
    Boolean isExistRow(GhCommitDTO ghCommitDTO);

}
